package com.hello.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hello.constants.SystemConstants;
import com.hello.domain.ResponseResult;
import com.hello.domain.dto.MenuDto;
import com.hello.domain.entity.Menu;
import com.hello.domain.entity.RoleMenu;
import com.hello.domain.vo.MenuVo;
import com.hello.domain.vo.RoleMenuTreeVo;
import com.hello.domain.vo.URoleMenuVo;
import com.hello.mapper.MenuMapper;
import com.hello.service.MenuService;
import com.hello.service.RoleMenuService;
import com.hello.utils.BeanCopyUtils;
import com.hello.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements MenuService {
    @Autowired
    private RoleMenuService roleMenuService;
    @Override
    public List<String> selectPermsByUserId(Long id) {
        if(id==SystemConstants.ADMIN_ID){
            LambdaQueryWrapper<Menu> queryWrapper=new LambdaQueryWrapper<>();
            queryWrapper.in(Menu::getMenuType, SystemConstants.MENU_TYPEC,SystemConstants.MENU_TYPEF);
            queryWrapper.eq(Menu::getStatus, SystemConstants.MENU_STATUS_NORMAL);
            List<Menu> menus=list(queryWrapper);
            List<String> perms = menus.stream()
                    .map(Menu::getPerms).collect(Collectors.toList());
            return perms;
        }
        return getBaseMapper().selectPermsByUserId(id);
    }

    @Override
    public List<Menu> selectRouterMenuTreeByUserId(Long userId) {
        List<Menu> menus = null;
        if(userId==SystemConstants.ADMIN_ID){
            menus=getBaseMapper().selectAllRouterMenu();
        }else {
            menus= getBaseMapper().selectRouterMenuTreeByUserId(userId);
        }
        //构建tree
        //先找出第一层的菜单  然后去找他们的子菜单设置到children属性中
        List<Menu> menuTree = builderMenuTree(menus,0L);
        return menuTree;
    }

    @Override
    public ResponseResult getAllMenu( MenuDto menuDto) {
        LambdaQueryWrapper<Menu> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.hasText(menuDto.getMenuName()),
                Menu::getMenuName, menuDto.getMenuName());
        queryWrapper.eq(StringUtils.hasText(menuDto.getStatus()),
                Menu::getStatus,menuDto.getStatus());
        queryWrapper.orderByAsc(Menu::getParentId).orderByAsc(Menu::getOrderNum);
        List<Menu> menus=list(queryWrapper);
        List<MenuVo> menuVos= BeanCopyUtils.copyBeanList(menus, MenuVo.class);
        return ResponseResult.okResult(menuVos);
    }

    @Override
    public ResponseResult addMenu(Menu menu){
        menu.setCreateBy(SecurityUtils.getUserId());
        menu.setCreateTime(new Date());
        save(menu);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult getMenuById(Long id) {
        Menu menu=getBaseMapper().selectById(id);
        MenuVo menuVo=BeanCopyUtils.copyBean(menu, MenuVo.class);
        return ResponseResult.okResult(menuVo);
    }

    @Override
    public ResponseResult updateMenu(Menu menu) {
        if(menu.getId().longValue()==menu.getParentId().longValue()){
            throw new RuntimeException("修改菜单失败，上级菜单不能选择自己");
        }else {
            menu.setUpdateBy(SecurityUtils.getUserId());
            menu.setUpdateTime(new Date());
            updateById(menu);
            return ResponseResult.okResult();
        }
    }

    @Override
    public ResponseResult deleteMenuById(Long id) {
        LambdaQueryWrapper<Menu> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(Menu::getParentId,id);
        List<Menu> menus=list(queryWrapper);
        if(menus.size()!=SystemConstants.SIZE){
            throw new RuntimeException("存在子菜单不允许删除");
        }
        getBaseMapper().deleteById(id);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult getMenuTree() {
        List<Menu> menus=list();
        List<RoleMenuTreeVo> menuVos=BeanCopyUtils.copyBeanList(menus, RoleMenuTreeVo.class);
        List<RoleMenuTreeVo> menuTreeVo=builderMenuTreeVo(menuVos, 0L);
        return ResponseResult.okResult(menuTreeVo);
    }

    @Override
    public ResponseResult roleMenuTreeById(Long id) {
        List<Menu> menus=list();
        List<RoleMenuTreeVo> menuVos=BeanCopyUtils.copyBeanList(menus, RoleMenuTreeVo.class);
        List<RoleMenuTreeVo> menuTreeVo=builderMenuTreeVo(menuVos, 0L);
        LambdaQueryWrapper<RoleMenu> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(RoleMenu::getRoleId, id);
        List<RoleMenu> roleMenus=roleMenuService.list(queryWrapper);
        List<Long> menuIds=roleMenus.stream()
                .map(roleMenu -> new Long(roleMenu.getMenuId()))
                .collect(Collectors.toList());
        return ResponseResult.okResult(new URoleMenuVo(menuTreeVo,menuIds));
    }

    private List<RoleMenuTreeVo> builderMenuTreeVo(List<RoleMenuTreeVo> menus, Long parentId) {
        List<RoleMenuTreeVo> menuTree = menus.stream()
                .filter(menu -> menu.getParentId().equals(parentId))//过滤操作
                .map(menu -> menu.setChildren(getChildrenVo(menu, menus)))
                .collect(Collectors.toList());
        return menuTree;
    }
    private List<Menu> builderMenuTree(List<Menu> menus, Long parentId) {
        List<Menu> menuTree = menus.stream()
                .filter(menu -> menu.getParentId().equals(parentId))//过滤操作
                .map(menu -> menu.setChildren(getChildren(menu, menus)))
                .collect(Collectors.toList());
        return menuTree;
    }
    /**
     * 获取存入参数的 子Menu集合
     * @param menu
     * @param menus
     * @return
     */
    private List<Menu> getChildren(Menu menu, List<Menu> menus) {
        List<Menu> childrenList = menus.stream()
                .filter(m -> m.getParentId().equals(menu.getId()))
                .map(m->m.setChildren(getChildren(m,menus)))
                .collect(Collectors.toList());
        return childrenList;
    }
    private List<RoleMenuTreeVo> getChildrenVo(RoleMenuTreeVo menu, List<RoleMenuTreeVo> menus) {
        List<RoleMenuTreeVo> childrenList = menus.stream()
                .filter(m -> m.getParentId().equals(menu.getId()))
                .map(m->m.setChildren(getChildrenVo(m,menus)))
                .collect(Collectors.toList());
        return childrenList;
    }
}
