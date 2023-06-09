package com.hello.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryListDto {
    private Long id;
    //分类名
    private String name;
    //状态0:正常,1禁用
    private String status;
    //描述
    private String description;
}
