package com.hello.service;

import com.hello.domain.ResponseResult;
import com.hello.domain.entity.User;

public interface BlogLoginService {
    ResponseResult login(User user);

    ResponseResult logout();
}
