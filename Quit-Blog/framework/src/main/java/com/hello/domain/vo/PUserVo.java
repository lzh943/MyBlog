package com.hello.domain.vo;

import com.hello.domain.entity.Role;
import com.hello.domain.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PUserVo {
    List<Long> roleIds;
    List<Role> roles;
    User user;
}
