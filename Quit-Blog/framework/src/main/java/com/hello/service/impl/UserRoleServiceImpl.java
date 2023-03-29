package com.hello.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hello.domain.entity.UserRole;
import com.hello.mapper.UserRoleMapper;
import com.hello.service.UserRoleService;
import org.springframework.stereotype.Service;

@Service
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, UserRole> implements UserRoleService {
}
