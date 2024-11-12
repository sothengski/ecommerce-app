package com.group01.ecommerce_app.dto;

import com.group01.ecommerce_app.model.Role;
import com.group01.ecommerce_app.model.User;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
// @AllArgsConstructor
@Data
public class UserDTO {

    private Long id;
    private String email;
    private String firstName;
    private String lastName;
    private String phone;
    private String address;
    private Role role;
    private Boolean active; // Include active status in response

    // Default constructor
    // public UserDTO() {
    // }

    // Constructor
    public UserDTO(Long id, String email, String firstName, String lastName,
            String phone, String address,
            Role role, Boolean active) {
        this.id = id;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.address = address;
        this.role = role;
        this.active = active;
    }

    public UserDTO(Long id, String email,
            Role role) {
        this.id = id;
        this.email = email;
        this.role = role;
    }

    // Getters and setters
    // Helper method for converting User to UserDTO
    public static UserDTO convertToUserDTO(User user) {
        return new UserDTO(
                user.getId(),
                user.getEmail(),
                user.getFirstName(),
                user.getLastName(),
                user.getPhone(),
                user.getShippingAddress(),
                new Role(user.getRole().getId(), user.getRole().getName()),
                user.getActive()); // Include active status);

    }
}
