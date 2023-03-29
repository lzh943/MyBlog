package com.hello.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hello.domain.ResponseResult;
import com.hello.domain.dto.GUserDto;
import com.hello.domain.dto.OUserDto;
import com.hello.domain.dto.PUserDto;
import com.hello.domain.dto.UserDto;
import com.hello.domain.entity.Role;
import com.hello.domain.entity.User;
import com.hello.domain.entity.UserRole;
import com.hello.domain.vo.GUserVo;
import com.hello.domain.vo.PUserVo;
import com.hello.domain.vo.PageVo;
import com.hello.domain.vo.UserInfoVo;
import com.hello.enums.AppHttpCodeEnum;
import com.hello.exception.SystemException;
import com.hello.mapper.UserMapper;
import com.hello.service.RoleService;
import com.hello.service.UserRoleService;
import com.hello.service.UserService;
import com.hello.utils.BeanCopyUtils;
import com.hello.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    UserRoleService userRoleService;
    @Autowired
    RoleService roleService;
    @Override
    public ResponseResult userInfo() {
        Long userId = SecurityUtils.getUserId();
        User user = getById(userId);
        UserInfoVo userInfoVo = BeanCopyUtils.copyBean(user, UserInfoVo.class);
        return ResponseResult.okResult(userInfoVo);
    }

    @Override
    public ResponseResult updateUserInfo(User user) {
        updateById(user);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult register(User user) {
        //对数据进行非空判断
        if (!StringUtils.hasText(user.getUserName())) {
            throw new SystemException(AppHttpCodeEnum.USERNAME_NOT_NULL);
        }
        if (!StringUtils.hasText(user.getPassword())) {
            throw new SystemException(AppHttpCodeEnum.PASSWORD_NOT_NULL);
        }
        if (!StringUtils.hasText(user.getEmail())) {
            throw new SystemException(AppHttpCodeEnum.EMAIL_NOT_NULL);
        }
        if (!StringUtils.hasText(user.getNickName())) {
            throw new SystemException(AppHttpCodeEnum.NICKNAME_NOT_NULL);
        }
        //对用户名和昵称进行是否重复的判断
        if(userNameExist(user.getUserName())){
            throw new SystemException(AppHttpCodeEnum.USERNAME_EXIST);
        }
        if(nickNameExist(user.getNickName())){
            throw new SystemException(AppHttpCodeEnum.NICKNAME_EXIST);
        }
        //对密码进行加密处理
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        save(user);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult getAllUser(Integer pageNum, Integer pageSize, GUserDto userDto) {
        LambdaQueryWrapper<User> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.hasText(userDto.getUserName()),
                User::getUserName,userDto.getUserName());
        queryWrapper.eq(StringUtils.hasText(userDto.getPhonenumber()),
                User::getPhonenumber, userDto.getPhonenumber());
        queryWrapper.eq(StringUtils.hasText(userDto.getStatus()),
                User::getStatus, userDto.getStatus());
        Page<User> userPage=new Page(pageNum,pageSize);
        page(userPage,queryWrapper);
        List<GUserVo> userVos=BeanCopyUtils.copyBeanList(userPage.getRecords(), GUserVo.class);
        return ResponseResult.okResult(new PageVo(userVos, userPage.getTotal()));
    }

    @Override
    public ResponseResult addUser(OUserDto oUserDto) {
        User user=BeanCopyUtils.copyBean(oUserDto, User.class);
        //对数据进行非空判断
        if (!StringUtils.hasText(user.getUserName())) {
            throw new SystemException(AppHttpCodeEnum.USERNAME_NOT_NULL);
        }
        if (!StringUtils.hasText(user.getPassword())) {
            throw new SystemException(AppHttpCodeEnum.PASSWORD_NOT_NULL);
        }
        if (!StringUtils.hasText(user.getEmail())) {
            throw new SystemException(AppHttpCodeEnum.EMAIL_NOT_NULL);
        }
        if (!StringUtils.hasText(user.getNickName())) {
            throw new SystemException(AppHttpCodeEnum.NICKNAME_NOT_NULL);
        }
        //对用户名和昵称进行是否重复的判断
        if(userNameExist(user.getUserName())){
            throw new SystemException(AppHttpCodeEnum.USERNAME_EXIST);
        }
        if(nickNameExist(user.getNickName())){
            throw new SystemException(AppHttpCodeEnum.NICKNAME_EXIST);
        }
        //对密码进行加密处理
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setCreateBy(SecurityUtils.getUserId());
        user.setCreateTime(new Date());
        save(user);
        List<UserRole> userRoles=oUserDto.getRoleIds().stream()
                .map(roleId->new UserRole(user.getId(), roleId))
                .collect(Collectors.toList());
        userRoleService.saveBatch(userRoles);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult deleteUserById(Long id) {
        getBaseMapper().deleteById(id);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult updateUser(PUserDto pUserDto) {
        User user=BeanCopyUtils.copyBean(pUserDto, User.class);
        if(userNameExist(user.getUserName())){
            throw new SystemException(AppHttpCodeEnum.USERNAME_EXIST);
        }
        if(nickNameExist(user.getNickName())){
            throw new SystemException(AppHttpCodeEnum.NICKNAME_EXIST);
        }
        user.setUpdateBy(SecurityUtils.getUserId());
        user.setUpdateTime(new Date());
        updateById(user);
        LambdaQueryWrapper<UserRole> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(UserRole::getUserId, user.getId());
        userRoleService.remove(queryWrapper);
        List<UserRole> userRoles=pUserDto.getRoleIds().stream()
                .map(roleId->new UserRole(user.getId(), roleId))
                .collect(Collectors.toList());
        userRoleService.saveBatch(userRoles);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult getUserById(long id) {
        LambdaQueryWrapper<UserRole> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(UserRole::getUserId, id);
        List<UserRole> userRoles=userRoleService.list(queryWrapper);
        List<Long> roleIds=userRoles.stream()
                .map(userRole -> new Long(userRole.getRoleId()))
                .collect(Collectors.toList());
        List<Role> roles=roleService.list();
        User user=getBaseMapper().selectById(id);
        return ResponseResult.okResult(new PUserVo(roleIds, roles, user));
    }

    @Override
    public ResponseResult changeSatus(UserDto userDto) {
        User user=new User();
        user.setId(userDto.getUserId());
        user.setStatus(userDto.getStatus());
        user.setUpdateBy(SecurityUtils.getUserId());
        user.setUpdateTime(new Date());
        updateById(user);
        return ResponseResult.okResult();
    }

    private boolean userNameExist(String username) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUserName, username);
        User user = getOne(queryWrapper);
        if (user != null) {
            return true;
        }
        return false;
    }

    private boolean nickNameExist(String nickname) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getNickName, nickname);
        User user = getOne(queryWrapper);
        if (user != null) {
            return true;
        }
        return false;
    }
}
