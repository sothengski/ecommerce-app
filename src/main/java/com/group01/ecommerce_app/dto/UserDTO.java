package com.group01.ecommerce_app.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.group01.ecommerce_app.model.Role;
import com.group01.ecommerce_app.model.User;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
// @AllArgsConstructor
@Builder
@Data
public class UserDTO implements Serializable {

    private Long id;
    private String email;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String firstName;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String lastName;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String phone;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String address;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Role role;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Boolean active; // Include active status in response

    // Custom logic to dynamically set fields to null
    public void excludeFields(String... fieldsToExclude) {
        for (String field : fieldsToExclude) {
            switch (field) {
                case "email" -> this.email = null;
                case "firstName" -> this.firstName = null;
                case "lastName" -> this.lastName = null;
                case "phone" -> this.phone = null;
                case "address" -> this.address = null;
                case "role" -> this.role = null;
                case "active" -> this.active = null;
                default -> throw new IllegalArgumentException("Invalid field: " + field);
            }
        }
    }

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
    public static UserDTO convertToUserDTO(final User user, String... fieldsToExclude) {
        UserDTO userDTO = new UserDTO(
                user.getId(),
                user.getEmail(),
                user.getFirstName(),
                user.getLastName(),
                user.getPhone(),
                user.getShippingAddress(),
                new Role(user.getRole().getId(), user.getRole().getName()),
                user.getActive());

        // Exclude specified fields
        userDTO.excludeFields(fieldsToExclude);
        return userDTO;
    }
}
