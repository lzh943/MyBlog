package com.hello.controller;

import com.hello.domain.ResponseResult;
import com.hello.domain.dto.AddRoleDto;
import com.hello.domain.dto.RoleDto;
import com.hello.domain.dto.UpdateRoleDto;
import com.hello.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/system/role")
public class RoleController {
    @Autowired
    RoleService roleService;

    /**
     * 查询所有角色信息
     * @param pageNum
     * @param pageSize
     * @param roleDto
     * @return
     */
    @GetMapping("/list")
    public ResponseResult getAllRole(Integer pageNum, Integer pageSize,RoleDto roleDto){
        return roleService.getAllRole(pageNum,pageSize,roleDto);
    }

    /**
     * 更改角色状态
     * @param roleDto
     * @return
     */
    @PutMapping("/changeStatus")
    public ResponseResult changeStatus(@RequestBody RoleDto roleDto){
        return roleService.changeStatus(roleDto);
    }

    /**
     * 新增角色
     * @param addRoleDto
     * @return
     */
    @PostMapping()
    public ResponseResult addRole(@RequestBody AddRoleDto addRoleDto){
        return roleService.addRole(addRoleDto);
    }

    /**
     * 通过id查询角色信息
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public ResponseResult getRoleById(@PathVariable Long id){
        return roleService.getRoleById(id);
    }

    /**
     * 修改角色信息
     * @param updateRoleDto
     * @return
     */
    @PutMapping()
    public ResponseResult updateRole(@RequestBody UpdateRoleDto updateRoleDto){
        return roleService.updateRole(updateRoleDto);
    }
    /**
     * 删除角色信息
     */
    @DeleteMapping("/{id}")
    public ResponseResult deleteRoleById(@PathVariable Long id){
        return roleService.deleteRoleById(id);
    }

    /**
     * 查询状态正常的角色
     * @return
     */
    @GetMapping("/listAllRole")
    public ResponseResult listAllRole(){
        return roleService.listAllRole();
    }
}
