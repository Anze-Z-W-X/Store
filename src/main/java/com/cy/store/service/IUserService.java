package com.cy.store.service;

import com.cy.store.entity.User;

public interface IUserService {
    //用户注册方法
    void reg(User user);

    //用户登录方法
    User login(String username,String password);

    void changePassword(Integer uid,String username,String oldPassword,String newPassword);

    User getByUid(Integer uid);

    void changeInfo(Integer uid,String username,User user);

    /**
     * 修改用户的头像
     * @param uid:用户的id
     * @param avatar:用户头像的路径
     * @param username:修改者的名称
     */
    void changeAvatar(Integer uid,String avatar,String username);

}
