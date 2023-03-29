package com.hello.controller;

import com.hello.annotation.SystemLog;
import com.hello.domain.ResponseResult;
import com.hello.domain.entity.User;
import com.hello.enums.AppHttpCodeEnum;
import com.hello.exception.SystemException;
import com.hello.service.BlogLoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController {
    @Autowired
    private BlogLoginService loginService;

    /**
     * 对登录用户进行认证和授权操作
     * @param user
     * @return
     */
    @PostMapping("/login")
    @SystemLog(BusinessName = "用户登录")
    public ResponseResult login(@RequestBody User user){
        if(!StringUtils.hasText(user.getUserName())){
            throw new SystemException(AppHttpCodeEnum.REQUIRE_USERNAME);
        }
        return loginService.login(user);
    }

    /**
     * 退出登录
     * @return
     */
    @PostMapping("/logout")
    @SystemLog(BusinessName = "退出登录")
    public ResponseResult logout(){
        return loginService.logout();
    }
}
