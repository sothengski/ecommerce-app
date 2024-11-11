package com.group01.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
// @AllArgsConstructor
@Data
public class UserCreateRequestDTO {
    private String email;
    private String firstName;
    private String lastName;
    private String password;
    private int roleId;

    // Getters and setters
}
