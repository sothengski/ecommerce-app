package com.group01.ecommerce_app.dto;

import com.group01.ecommerce_app.model.Role;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
// @AllArgsConstructor
@Data
public class UserLoginDTO {
    private Long id;
    private String email;
    private String password;
    private Role role;
}
