package com.hello.service;

import com.hello.domain.ResponseResult;
import com.hello.domain.entity.User;

public interface AdminLoginService {
    ResponseResult login(User user);

    ResponseResult getInfo();

    ResponseResult getRouters();

    ResponseResult logout();
}
