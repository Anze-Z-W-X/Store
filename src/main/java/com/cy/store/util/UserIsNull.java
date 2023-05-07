package com.cy.store.util;

import com.cy.store.entity.User;
import com.cy.store.service.ex.UserNotFoundException;

public class UserIsNull {
    public static void judge(User res){
        if(res==null||res.getIsDelete()==1)throw new UserNotFoundException("用户数据不存在!");
    }
}
