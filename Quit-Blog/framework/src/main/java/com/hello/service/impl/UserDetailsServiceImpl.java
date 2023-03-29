package com.hello.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.hello.domain.LoginUser;
import com.hello.mapper.MenuMapper;
import com.hello.mapper.UserMapper;
import com.hello.domain.entity.User;
import com.hello.utils.SecurityUtils;
import com.hello.constants.SystemConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private MenuMapper menuMapper;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        LambdaQueryWrapper<User> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUserName, username);
        User user=userMapper.selectOne(queryWrapper);
        if(Objects.isNull(user)){
            throw new RuntimeException("该用户名不存在");
        }
        if(user.getType().equals(SystemConstants.ADMIN)) {
            List<String> permissions = menuMapper.selectPermsByUserId(user.getId());
            return new LoginUser(user,permissions);
        }
        return new LoginUser(user,null);
    }
}
