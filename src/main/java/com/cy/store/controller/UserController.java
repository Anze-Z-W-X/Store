package com.cy.store.controller;

import com.cy.store.controller.ex.FileEmptyException;
import com.cy.store.controller.ex.FileTypeException;
import com.cy.store.controller.ex.FileUploadIOException;
import com.cy.store.entity.User;
import com.cy.store.service.IUserService;
import com.cy.store.util.JsonResult;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
public class UserController extends BaseController{
    @Autowired
    private IUserService userService;
    @RequestMapping("/users/reg")
    public JsonResult<Void> reg(User user){
        //创建响应结果对象
        userService.reg(user);
        return new JsonResult<>(OK);
    }

    @RequestMapping("/users/login")
    public JsonResult<User> login(String username, String password, HttpSession session){
        User data = userService.login(username,password);
        session.setAttribute("uid",data.getUid());
        session.setAttribute("username",data.getUsername());
        return new JsonResult<User>(OK,data);
    }

    @RequestMapping("/users/change_password")
    public JsonResult<Void> changePassword(String oldPassword,String newPassword,HttpSession session){
        Integer uid = getUidFromSession(session);
        String username = getUsernameFromSession(session);
        userService.changePassword(uid,username,oldPassword,newPassword);
        return new JsonResult<>(OK);
    }

    @RequestMapping("/users/get_by_uid")
    public JsonResult<User> getByUid(HttpSession session){
        User data =
                userService.getByUid(getUidFromSession(session));
        return new JsonResult<>(OK,data);
    }

    @RequestMapping("/users/change_info")
    public JsonResult<Void> changeInfo(User user,HttpSession session){
        //user对象有四部分的数据：username,phone,email,gender
        //uid数据需要再次封装到user中
        Integer uid = getUidFromSession(session);
        String username = getUsernameFromSession(session);
        userService.changeInfo(uid,username,user);
        return new JsonResult<>(OK);
    }

//    public static final int AVATAR_MAX_SIZE=10*1024*1024;可以在application.yml中配置
    public static final List<String> AVATAR_TYPE=
        new ArrayList<>();
    static {
        AVATAR_TYPE.add("image/jpeg");
        AVATAR_TYPE.add("image/png");
        AVATAR_TYPE.add("image/bmp");
        AVATAR_TYPE.add("image/gif");
    }
    /**
     * MultipartFile接口是由SpringMVC提供的，包装了获取文件类型的数据
     * SpringBoot整合了SpringMVC，只需要在处理请求的方法参数列表上声明
     * 一个参数类型为MultipartFile，SpringBoot会自动将传给服务器的文件赋值
     * 参数上如果名称不一致可使用@RequestParam
     * @param session
     * @param file
     * @return
     */
    @RequestMapping("/users/change_avatar")
    public JsonResult<String> changeAvatar(HttpSession session,
                                           @RequestParam("file") MultipartFile file){
        if(file.isEmpty())throw new FileEmptyException("文件为空");
        //判断文件累心是否为我们所规定的后缀类型
        String contentType = file.getContentType();
        //如果集合包含某个元素则返回true
        if(!AVATAR_TYPE.contains(contentType))throw new FileTypeException("文件类型不支持!");
        //上传的文件.../upload/文件.png
        String parent = session.getServletContext().getRealPath("upload");
        System.out.println("parent = " + parent);
        //File对象指向这个路径,File是否存在
        File dir = new File(parent);
        if(!dir.exists())
            dir.mkdirs();//创建当前目录
        //获取文件的名称,使用UUID来生成一个新的字符串作为文件名
        //例如:avatar01.png
        String originalFileName = file.getOriginalFilename();
        System.out.println("originalFileName = " + originalFileName);
        String suffix = originalFileName.substring(originalFileName.lastIndexOf("."));
        String fileName = UUID.randomUUID().toString().toUpperCase()+suffix;
        File dest = new File(dir,fileName);//是一个空文件
        //参数file中的数据写入空文件中
        try {
            file.transferTo(dest);
        } catch (IOException e) {
            throw new FileUploadIOException("文件读写异常!");
        }
        Integer uid = getUidFromSession(session);
        String username = getUsernameFromSession(session);
        //返回头像的路径/upload/test.png
        String avatar = "/upload/"+fileName;
        userService.changeAvatar(uid,avatar,username);
        return new JsonResult<>(OK,avatar);


    }

}
