package com.hello.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hello.domain.ResponseResult;
import com.hello.domain.dto.MenuDto;
import com.hello.domain.entity.Menu;

import java.util.List;

public interface MenuService extends IService<Menu> {
    List<String> selectPermsByUserId(Long id);

    List<Menu> selectRouterMenuTreeByUserId(Long userId);

    ResponseResult getAllMenu( MenuDto menuDto);

    ResponseResult addMenu(Menu menu);

    ResponseResult getMenuById(Long id);

    ResponseResult updateMenu(Menu menu);

    ResponseResult deleteMenuById(Long id);

    ResponseResult getMenuTree();

    ResponseResult roleMenuTreeById(Long id);
}
