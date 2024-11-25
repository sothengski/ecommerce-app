package com.group01.ecommerce_app.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
// @AllArgsConstructor
@Data
public class UserCreateRequestDTO {
    private String email;
    private String firstName;
    private String lastName;
    private String phone;
    private String address;
    private String password;
    private Integer roleId;

    // Getters and setters
}
