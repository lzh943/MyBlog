package com.hello.service.impl;

import com.hello.domain.LoginUser;
import com.hello.domain.ResponseResult;
import com.hello.domain.entity.Menu;
import com.hello.domain.entity.User;
import com.hello.domain.vo.AdminUserInfoVo;
import com.hello.domain.vo.RoutersVo;
import com.hello.domain.vo.UserInfoVo;
import com.hello.enums.AppHttpCodeEnum;
import com.hello.exception.SystemException;
import com.hello.service.AdminLoginService;
import com.hello.service.MenuService;
import com.hello.service.RoleService;
import com.hello.utils.BeanCopyUtils;
import com.hello.utils.JwtUtil;
import com.hello.utils.RedisCache;
import com.hello.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
public class AdminLoginServiceImpl implements AdminLoginService {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private RedisCache redisCache;
    @Autowired
    private RoleService roleService;
    @Autowired
    private MenuService menuService;
    @Override
    public ResponseResult login(User user) {
        UsernamePasswordAuthenticationToken authenticationToken=new
                UsernamePasswordAuthenticationToken(user.getUserName(), user.getPassword());
        //会调用UserDetailsServiceImpl进行验证
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);
        //判断用户是否认证成功
        if(Objects.isNull(authenticate)){
            throw new SystemException(AppHttpCodeEnum.LOGIN_ERROR);
        }
        //将用户信息存入redis
        LoginUser loginUser =(LoginUser) authenticate.getPrincipal();
        String userId= loginUser.getUser().getId().toString();
        redisCache.setCacheObject("adminlogin:"+userId, loginUser);
        //生成token,并封装为vo返回
        String jwt= JwtUtil.createJWT(userId);
        Map<String,String> map=new HashMap<>();
        map.put("token", jwt);
        return ResponseResult.okResult(map);
    }

    @Override
    public ResponseResult logout() {
        Long userId=SecurityUtils.getUserId();
        redisCache.deleteObject("adminlogin:"+userId);
        return ResponseResult.okResult();
    }
    @Override
    public ResponseResult getInfo() {
        LoginUser loginUser = SecurityUtils.getLoginUser();
        //根据用户id查询权限信息
        List<String> perms = menuService.selectPermsByUserId(loginUser.getUser().getId());
        //根据用户id查询角色信息
        List<String> roleKeyList = roleService.selectRoleKeyByUserId(loginUser.getUser().getId());
        //获取用户信息
        UserInfoVo userInfoVo= BeanCopyUtils.copyBean(loginUser.getUser(), UserInfoVo.class);
        AdminUserInfoVo adminUserInfoVo=new AdminUserInfoVo(perms,roleKeyList,userInfoVo);
        return ResponseResult.okResult(adminUserInfoVo);
    }

    @Override
    public ResponseResult getRouters() {
        Long userId = SecurityUtils.getUserId();
        //查询menu 结果是tree的形式
        List<Menu> menus = menuService.selectRouterMenuTreeByUserId(userId);
        //封装数据返回
        return ResponseResult.okResult(new RoutersVo(menus));
    }

}
