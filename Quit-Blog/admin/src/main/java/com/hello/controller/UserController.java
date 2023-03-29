package com.hello.controller;

import com.hello.domain.ResponseResult;
import com.hello.domain.dto.*;
import com.hello.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("system/user")
public class UserController {
    @Autowired
    UserService userService;

    /**
     * 查询所有用户
     * @param pageNum
     * @param pageSize
     * @param userDto
     * @return
     */
    @GetMapping("/list")
    public ResponseResult getAllUser(Integer pageNum, Integer pageSize, GUserDto userDto){
        return userService.getAllUser(pageNum,pageSize,userDto);
    }

    /**
     * 新增用户
     * @param oUserDto
     * @return
     */
    @PostMapping()
    public ResponseResult addUser(@RequestBody OUserDto oUserDto){
        return userService.addUser(oUserDto);
    }

    /**
     * 删除用户
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public ResponseResult deleteUserById(@PathVariable Long id){
        return userService.deleteUserById(id);
    }
    @GetMapping("/{id}")
    public ResponseResult getUserById(@PathVariable long id){
        return userService.getUserById(id);
    }
    /**
     * 修改用户
     * @param pUserDto
     * @return
     */
    @PutMapping()
    public ResponseResult updateUser(@RequestBody PUserDto pUserDto){
        return userService.updateUser(pUserDto);
    }

    /**
     * 修改用户状态
     * @param userDto
     * @return
     */
    @PutMapping("/changeStatus")
    public ResponseResult changeStatus(@RequestBody UserDto userDto){
        return userService.changeSatus(userDto);
    }
}
