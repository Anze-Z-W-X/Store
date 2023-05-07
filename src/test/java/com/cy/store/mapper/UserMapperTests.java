package com.cy.store.mapper;

import com.cy.store.entity.User;
import com.cy.store.service.IUserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;

@SpringBootTest
public class UserMapperTests {
    @Autowired
    private UserMapper userMapper;

    @Test
    void insert(){
//        User user = new User();
//        user.setUsername("tim");
//        user.setPassword("123");
//        Integer rows = userMapper.insert(user);
//        System.out.println(rows);
    }

    @Test
    void findByUserId(){
//        User tim = userMapper.findByUserName("tim");
//        System.out.println("tim = " + tim);
    }@Autowired
    IUserService userService;

    @Test
    void login(){
        User user = userService.login("赵六","123");
        System.out.println(user);
    }

    @Test
    void findByUid(){
        User byUid = userMapper.findByUid(10);
        System.out.println("byUid = " + byUid);
    }

    @Test
    void updatePasswordByUid(){
        userMapper.updatePasswordByUid(1,"2277","a",new Date());
    }

    @Test
    void updateInfoByUid(){
        User user = new User();
        user.setUid(10);
        user.setPhone("199770000");
        user.setEmail("zhaoliu@qq.com");
        user.setGender(1);
        Integer integer = userMapper.updateInfoByUid(user);
    }

    @Test
    void updateAvatarByUid(){
        userMapper.updateAvatarByUid(10,"/upload/avatar.png","管理员",new Date());
    }
}
