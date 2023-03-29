package com.hello.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    //角色状态（0正常 1停用）
    private String status;
    //角色id
    private Long userId;
}
