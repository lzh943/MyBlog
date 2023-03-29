package com.hello.controller;

import com.hello.domain.LoginUser;
import com.hello.domain.ResponseResult;
import com.hello.domain.entity.User;
import com.hello.domain.vo.AdminUserInfoVo;
import com.hello.domain.vo.UserInfoVo;
import com.hello.enums.AppHttpCodeEnum;
import com.hello.exception.SystemException;
import com.hello.service.AdminLoginService;
import com.hello.service.MenuService;
import com.hello.service.RoleService;
import com.hello.utils.BeanCopyUtils;
import com.hello.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class LoginController {
    @Autowired
    private AdminLoginService adminLoginService;

    /**
     * 用户后台登录
     * @param user
     * @return
     */
    @PostMapping("/user/login")
    public ResponseResult login(@RequestBody User user){
        if(!StringUtils.hasText(user.getUserName())){
            throw new SystemException(AppHttpCodeEnum.REQUIRE_USERNAME);
        }
        return adminLoginService.login(user);
    }

    /**
     * 退出登录
     * @return
     */
    @PostMapping("/user/logout")
    public ResponseResult logout(){
        return adminLoginService.logout();
    }

    /**
     * 获取用户角色信息,权限信息，基本信息
     * @return
     */
    @GetMapping("/getInfo")
    public ResponseResult getInfo(){
        return adminLoginService.getInfo();
    }

    /**
     *获取用户可以操作的菜单列表
     * @return
     */
    @GetMapping("/getRouters")
    public ResponseResult getRouters(){
        return adminLoginService.getRouters();
    }
}
