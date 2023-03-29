package com.hello.domain.vo;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class RoleMenuTreeVo {
    private Long id;
    //菜单名称
    @JSONField(name = "label")
    private String menuName;
    //父菜单ID
    private Long parentId;
    private List<RoleMenuTreeVo> children;
}
