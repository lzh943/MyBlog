package com.hello.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class URoleMenuVo {
    List<RoleMenuTreeVo> menus;
    List<Long> checkedKeys;
}
