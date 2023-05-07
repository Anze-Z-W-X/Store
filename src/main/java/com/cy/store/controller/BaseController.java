package com.cy.store.controller;

import com.cy.store.controller.ex.*;
import com.cy.store.service.ex.*;
import com.cy.store.util.JsonResult;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.ExceptionHandler;

//控制层类的基类
public class BaseController {
    public static final Integer OK = 200;
    @ExceptionHandler({ServiceException.class,FileUploadException.class})
    public JsonResult<Void> handlerException(Throwable e){
        JsonResult<Void> result = new JsonResult<>(e);
        result.setMessage(e.getMessage());
        if(e instanceof UsernameDuplicatedException){
            result.setState(4000);
        } else if (e instanceof InsertException) {
            result.setState(5000);
        } else if (e instanceof UserNotFoundException) {
            result.setState(5001);
        } else if (e instanceof PasswordNotMatchException) {
            result.setState(5002);
        } else if (e instanceof UpdateException) {
            result.setState(5001);
        } else if (e instanceof FileEmptyException) {
            result.setState(6000);
        } else if (e instanceof FileSizeException) {
            result.setState(6001);
        } else if (e instanceof FileTypeException) {
            result.setState(6002);
        } else if (e instanceof FileStateException) {
            result.setState(6003);
        } else if (e instanceof FileUploadIOException) {
            result.setState(6004);
        }
        return result;
    }
    //获取session对象中的uid
    protected final Integer getUidFromSession(HttpSession session){
        String uid = session.getAttribute("uid").toString();
        return Integer.valueOf(uid);
    }

    protected final String getUsernameFromSession(HttpSession session){
        return session.getAttribute("username").toString();
    }
}
