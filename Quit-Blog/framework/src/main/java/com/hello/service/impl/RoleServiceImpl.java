package com.hello.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hello.constants.SystemConstants;
import com.hello.domain.ResponseResult;
import com.hello.domain.dto.AddRoleDto;
import com.hello.domain.dto.RoleDto;
import com.hello.domain.dto.UpdateRoleDto;
import com.hello.domain.entity.Role;
import com.hello.domain.entity.RoleMenu;
import com.hello.domain.vo.AllRoleVo;
import com.hello.domain.vo.PageVo;
import com.hello.domain.vo.RoleVo;
import com.hello.mapper.RoleMapper;
import com.hello.service.RoleMenuService;
import com.hello.service.RoleService;
import com.hello.utils.BeanCopyUtils;
import com.hello.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {
    @Autowired
    private RoleMenuService roleMenuService;
    @Override
    public List<String> selectRoleKeyByUserId(Long id) {
        if(id== SystemConstants.ADMIN_ID){
            List<String> roleKeys = new ArrayList<>();
            roleKeys.add("admin");
            return roleKeys;
        }
        return getBaseMapper().selectRoleKeyByUserId(id);
    }

    @Override
    public ResponseResult getAllRole(Integer pageNum, Integer pageSize, RoleDto roleDto) {
        LambdaQueryWrapper<Role> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.hasText(roleDto.getRoleName()),
                Role::getRoleName,roleDto.getRoleName());
        queryWrapper.eq(StringUtils.hasText(roleDto.getStatus()),
                Role::getStatus,roleDto.getStatus());
        queryWrapper.orderByAsc(Role::getRoleSort );
        Page<Role> rolePage=new Page(pageNum,pageSize);
        page(rolePage, queryWrapper);
        List<RoleVo> roleVos= BeanCopyUtils.copyBeanList(rolePage.getRecords(), RoleVo.class);
        return ResponseResult.okResult(new PageVo(roleVos, rolePage.getTotal()));
    }

    @Override
    public ResponseResult changeStatus(RoleDto roleDto) {
        Role role=new Role();
        role.setId(roleDto.getRoleId());
        role.setStatus(roleDto.getStatus());
        role.setUpdateBy(SecurityUtils.getUserId());
        role.setUpdateTime(new Date());
        updateById(role);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult addRole(AddRoleDto addRoleDto) {
        Role role=BeanCopyUtils.copyBean(addRoleDto, Role.class);
        role.setCreateTime(new Date());
        role.setCreateBy(SecurityUtils.getUserId());
        save(role);
        List<RoleMenu> roleMenus=addRoleDto.getMenuIds().stream()
                .map(menuId->new RoleMenu(role.getId(), menuId))
                .collect(Collectors.toList());
        roleMenuService.saveBatch(roleMenus);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult getRoleById(Long id) {
        Role role=getBaseMapper().selectById(id);
        RoleVo roleVo=BeanCopyUtils.copyBean(role, RoleVo.class);
        return ResponseResult.okResult(roleVo);
    }

    @Override
    public ResponseResult updateRole(UpdateRoleDto updateRoleDto) {
        Role role=BeanCopyUtils.copyBean(updateRoleDto, Role.class);
        role.setUpdateBy(SecurityUtils.getUserId());
        role.setUpdateTime(new Date());
        updateById(role);
        LambdaQueryWrapper<RoleMenu> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(RoleMenu::getRoleId, role.getId());
        roleMenuService.remove(queryWrapper);
        List<RoleMenu> roleMenus=updateRoleDto.getMenuIds().stream()
                .map(menuId->new RoleMenu(role.getId(), menuId))
                .collect(Collectors.toList());
        roleMenuService.saveBatch(roleMenus);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult deleteRoleById(Long id) {
        getBaseMapper().deleteById(id);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult listAllRole() {
        LambdaQueryWrapper<Role> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(Role::getStatus,SystemConstants.ROLE_STATUS);
        List<Role> roleList=list(queryWrapper);
        List<AllRoleVo> allRoleVos=BeanCopyUtils.copyBeanList(roleList, AllRoleVo.class);
        return ResponseResult.okResult(allRoleVos);
    }
}
