package com.cy.store.service.impl;

import com.cy.store.entity.User;
import com.cy.store.mapper.UserMapper;
import com.cy.store.service.IUserService;
import com.cy.store.service.ex.*;
import com.cy.store.util.UserIsNull;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.Date;
import java.util.UUID;

@Service
public class IUserServiceImpl implements IUserService {
    @Resource
    private UserMapper userMapper;

    @Override
    public void reg(User user) {
        User byUserName = userMapper.findByUserName(user.getUsername());
        System.out.println(byUserName);
        if(byUserName!=null)throw new UsernameDuplicatedException("用户名被占用!");
        //密码加密处理:md5-->(盐值+pwd+盐值)md5加密三次
        String oldPassword = user.getPassword();
        //获取盐值
        String salt = UUID.randomUUID().toString().toUpperCase();
        //(将密码与盐值作为整体加密)
        String md5Password = getMD5Password(oldPassword, salt);
        //将加密后的密码重新补全设置到user对象中
        user.setPassword(md5Password);
        //盐值的记录
        user.setSalt(salt);
        //补全数据
        user.setIsDelete(0);
        user.setCreatedUser(user.getUsername());
        Date date = new Date();
        user.setCreatedTime(date);
        user.setModifiedUser(user.getUsername());
        user.setModifiedTime(date);

        Integer rows = userMapper.insert(user);
        if(rows!=1)throw new InsertException("在用户注册过程中产生了未知的异常!");

    }

    @Override
    public User login(String username, String password) {
        //根据用户名称来查询用户的数据是否存在，如果不存在则抛出异常
        User result = userMapper.findByUserName(username);
        //判断result是否为空或者是否注销
        UserIsNull.judge(result);

        //检测用户的密码是否匹配
        String pwd = result.getPassword();
        String md5Pwd = getMD5Password(password,result.getSalt());
        if(!md5Pwd.equals(pwd))throw new PasswordNotMatchException("用户密码错误!");
        //判断用户是否注销
        if(result.getIsDelete()==1)throw new UserNotFoundException("用户数据不存在!");
        //信息压缩
        User user = new User();
        user.setUid(result.getUid());
        user.setUsername(result.getUsername());
        user.setAvatar(result.getAvatar());
        return user;
    }

    @Override
    public void changePassword(Integer uid, String username, String oldPassword, String newPassword) {
        User res = userMapper.findByUid(uid);
        UserIsNull.judge(res);
        System.out.println(oldPassword);
        String oldMd5Password = getMD5Password(oldPassword,res.getSalt());
        System.out.println(oldMd5Password);
        if(!(res.getPassword().equals(oldMd5Password)))throw new PasswordNotMatchException("密码错误!");
        //将新的密码设置到数据库中
        String newMd5Password = getMD5Password(newPassword,res.getSalt());

        Integer rows = userMapper.updatePasswordByUid(uid, newMd5Password, username, new Date());
        if(rows!=1)throw new UpdateException("更新数据时产生未知的异常!");
    }

    @Override
    public User getByUid(Integer uid) {
        User res = userMapper.findByUid(uid);
        UserIsNull.judge(res);
        User user = new User();
        user.setUsername(res.getUsername());
        user.setPhone(res.getPhone());
        user.setEmail(res.getEmail());
        user.setGender(res.getGender());
        return user;
    }

    @Override
    public void changeInfo(Integer uid, String username, User user) {
        User res = userMapper.findByUid(uid);
        UserIsNull.judge(res);
        user.setUid(res.getUid());
        user.setUsername(res.getUsername());
        user.setModifiedUser(username);
        user.setModifiedTime(new Date());

        Integer rows = userMapper.updateInfoByUid(user);
        if(rows!=1)throw new UpdateException("更新数据时产生未知的异常!");
        
    }

    @Override
    public void changeAvatar(Integer uid, String avatar, String username) {
        // 查询当前的用户数据是否存在
        User res = userMapper.findByUid(uid);
        UserIsNull.judge(res);
        Integer rows = userMapper.updateAvatarByUid(uid,avatar,username,new Date());
        if(rows!=1)throw new UpdateException("更新用户头像产生未知的异常");

    }

    private String getMD5Password(String pwd,String salt){
        for(int i=0;i<3;i++)
            pwd = DigestUtils.md5DigestAsHex((salt+pwd+salt).getBytes()).toUpperCase();
        return pwd;
    }

}
