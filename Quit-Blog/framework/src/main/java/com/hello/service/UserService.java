package com.hello.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hello.domain.ResponseResult;
import com.hello.domain.dto.GUserDto;
import com.hello.domain.dto.OUserDto;
import com.hello.domain.dto.PUserDto;
import com.hello.domain.dto.UserDto;
import com.hello.domain.entity.User;

public interface UserService extends IService<User> {
    ResponseResult userInfo();

    ResponseResult updateUserInfo(User user);

    ResponseResult register(User user);

    ResponseResult getAllUser(Integer pageNum, Integer pageSize, GUserDto userDto);

    ResponseResult addUser(OUserDto oUserDto);

    ResponseResult deleteUserById(Long id);

    ResponseResult updateUser(PUserDto pUserDto);

    ResponseResult getUserById(long id);

    ResponseResult changeSatus(UserDto userDto);
}
