package com.hello.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hello.domain.ResponseResult;
import com.hello.domain.dto.AddRoleDto;
import com.hello.domain.dto.RoleDto;
import com.hello.domain.dto.UpdateRoleDto;
import com.hello.domain.entity.Role;

import java.util.List;

public interface RoleService extends IService<Role> {
    List<String> selectRoleKeyByUserId(Long id);

    ResponseResult getAllRole(Integer pageNum, Integer pageSize, RoleDto roleDto);

    ResponseResult changeStatus(RoleDto roleDto);

    ResponseResult addRole(AddRoleDto addRoleDto);

    ResponseResult getRoleById(Long id);

    ResponseResult updateRole(UpdateRoleDto updateRoleDto);

    ResponseResult deleteRoleById(Long id);

    ResponseResult listAllRole();
}
