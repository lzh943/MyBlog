package com.hello.controller;

import com.hello.domain.ResponseResult;
import com.hello.domain.dto.MenuDto;
import com.hello.domain.entity.Menu;
import com.hello.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/system/menu")
public class MenuController {
    @Autowired
    private MenuService menuService;
    /**
     * 查询所有菜单
     * @param menuDto
     * @return
     */
    @GetMapping("/list")
    public ResponseResult getAllMenu(MenuDto menuDto){
        return menuService.getAllMenu(menuDto);
    }

    /**
     * 查询菜单树
     * @return
     */
    @GetMapping("/treeselect")
    public ResponseResult getMenuTree(){
        return menuService.getMenuTree();
    }

    /**
     * 查询菜单树和具有的权限id
     * @param id
     * @return
     */
    @GetMapping("roleMenuTreeselect/{id}")
    public ResponseResult roleMenuTreeById(@PathVariable Long id){
        return menuService.roleMenuTreeById(id);
    }
    /**
     * 添加菜单
     * @param menu
     * @return
     */
    @PostMapping()
    public ResponseResult addMenu(@RequestBody Menu menu){
        return menuService.addMenu(menu);
    }

    /**
     * 查询单个菜单信息
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public ResponseResult getMenuById(@PathVariable Long id){
        return menuService.getMenuById(id);
    }

    /**
     * 修改菜单
     * @param menu
     * @return
     */
    @PutMapping()
    public ResponseResult updateMenu(@RequestBody Menu menu){
        return menuService.updateMenu(menu);
    }

    /**
     * 删除菜单
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public ResponseResult deleteMenuById(@PathVariable Long id){
        return menuService.deleteMenuById(id);
    }
}
