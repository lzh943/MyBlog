package com.hello.service.impl;

import com.hello.domain.LoginUser;
import com.hello.domain.ResponseResult;
import com.hello.domain.entity.User;
import com.hello.domain.vo.BlogUserLoginVo;
import com.hello.domain.vo.UserInfoVo;
import com.hello.enums.AppHttpCodeEnum;
import com.hello.exception.SystemException;
import com.hello.service.BlogLoginService;
import com.hello.utils.BeanCopyUtils;
import com.hello.utils.JwtUtil;
import com.hello.utils.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class BlogLoginServiceImpl implements BlogLoginService {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    RedisCache redisCache;
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
        redisCache.setCacheObject("bloglogin:"+userId, loginUser);
        //生成token,并封装为vo返回
        String jwt=JwtUtil.createJWT(userId);
        BlogUserLoginVo userLoginVo=new BlogUserLoginVo(jwt,
                BeanCopyUtils.copyBean(loginUser.getUser(), UserInfoVo.class));
        return ResponseResult.okResult(userLoginVo);
    }

    @Override
    public ResponseResult logout() {
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        LoginUser loginUser =(LoginUser) authentication.getPrincipal();
        String userId=loginUser.getUser().getId().toString();
        redisCache.deleteObject("bloglogin:"+userId);
        return ResponseResult.okResult();
    }
}
