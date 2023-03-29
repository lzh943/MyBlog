package com.hello.controller;

import com.hello.annotation.SystemLog;
import com.hello.domain.ResponseResult;
import com.hello.domain.entity.User;
import com.hello.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    /**
     * 查看个人信息
     * @return
     */
    @GetMapping("/userInfo")
    @SystemLog(BusinessName = "查看个人信息")
    public ResponseResult userInfo(){
        return userService.userInfo();
    }

    /**
     * 更新个人信息
     * @param user
     * @return
     */
    @PutMapping("/userInfo")
    @SystemLog(BusinessName = "更新个人信息")
    public ResponseResult updateUserInfo(@RequestBody User user){
        return userService.updateUserInfo(user);
    }

    /**
     * 用户注册
     * @param user
     * @return
     */
    @PostMapping("/register")
    @SystemLog(BusinessName = "用户注册")
    public ResponseResult register(@RequestBody User user){
        return userService.register(user);
    }
}
