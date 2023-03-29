package com.hello.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hello.domain.entity.RoleMenu;
import com.hello.mapper.RoleMenuMapper;
import com.hello.service.RoleMenuService;
import org.springframework.stereotype.Service;

@Service
public class RoleMenuServiceImpl extends ServiceImpl<RoleMenuMapper,RoleMenu> implements RoleMenuService {
}
