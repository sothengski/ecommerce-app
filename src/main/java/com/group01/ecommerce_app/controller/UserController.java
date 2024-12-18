package com.group01.ecommerce_app.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.group01.ecommerce_app.dto.ApiResponse;
import com.group01.ecommerce_app.dto.UserCreateRequestDTO;
import com.group01.ecommerce_app.dto.UserDTO;
import com.group01.ecommerce_app.model.Cart;
import com.group01.ecommerce_app.model.CartRepository;
import com.group01.ecommerce_app.model.Role;
import com.group01.ecommerce_app.model.RoleRepository;
import com.group01.ecommerce_app.model.User;
import com.group01.ecommerce_app.model.UserRepository;

@CrossOrigin(origins = "http://localhost:8081")
@RestController
@RequestMapping("/api")
public class UserController {

    // @Autowired
    private final UserRepository userRepository;

    // @Autowired
    private final RoleRepository roleRepository;

    // @Autowired
    private final CartRepository cartRepository;

    public UserController(RoleRepository roleRepository, UserRepository userRepository, CartRepository cartRepository) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.cartRepository = cartRepository;
    }

    @GetMapping("/users")
    public ResponseEntity<ApiResponse<List<UserDTO>>> getAllUsers() {
        try {
            // List<UserDTO> users = userRepository.findAll().stream()
            // .map(UserDTO::convertToUserDTO)
            // // using convertToUserDTO instead of
            // // user -> new UserDTO(
            // // user.getId(),
            // // user.getEmail(),
            // // user.getFirstName(),
            // // user.getLastName(),
            // // user.getPhone(),
            // // user.getShippingAddress(),
            // // user.getRole()))
            // //
            // .collect(Collectors.toList());

            List<User> users = userRepository.findAll();
            List<UserDTO> userDTOs = new ArrayList<>();
            for (User user : users) {
                userDTOs.add(UserDTO.convertToUserDTO(user));
            }

            // List of users is Empty
            if (users.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            // List of users have the data
            return new ResponseEntity<>(new ApiResponse<>(true, "User retrieved successfully",
                    userDTOs), HttpStatus.OK);
            // return new ResponseEntity<>(users, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Error Getting all users data", e.getMessage()));
        }
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<ApiResponse<UserDTO>> getUserById(@PathVariable("id") Long id) {
        try {
            Optional<User> userData = userRepository.findById(id);
            if (userData.isPresent()) {
                User userTemp = userData.get();
                return new ResponseEntity<>(new ApiResponse<>(true, "User retrieved successfully",
                        UserDTO.convertToUserDTO(userTemp)), HttpStatus.OK);
            }
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Error getting a user data", e.getMessage()));
        }
    }

    // Get all users by role ID
    @GetMapping("/roles/{roleId}/users")
    public ResponseEntity<ApiResponse<List<UserDTO>>> getUsersByRoleId(@PathVariable("roleId") Long roleId) {

        List<User> users = userRepository.findByRoleId(roleId);
        List<UserDTO> userDTOs = new ArrayList<>();
        for (User user : users) {
            userDTOs.add(UserDTO.convertToUserDTO(user));
        }

        // List of users is Empty
        if (users.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        // List of users have the data
        return new ResponseEntity<>(new ApiResponse<>(true, "User retrieved successfully",
                userDTOs), HttpStatus.OK);
    }

    @PostMapping("/users")
    public ResponseEntity<ApiResponse<UserDTO>> createUser(@RequestBody UserCreateRequestDTO userData) {
        try {
            // // Encrypt the password
            // // user.setPassword(passwordEncoder.encode(user.getPassword()));
            // // Check if the email already exists
            if (userRepository.findByEmail(userData.getEmail()).isPresent()) {
                return new ResponseEntity<>(new ApiResponse<>(false, "User creation failed", "Email already in use"),
                        HttpStatus.BAD_REQUEST);

            }
            // // Find the role by ID
            Optional<Role> roleOpt = roleRepository.findById(userData.getRoleId());
            if (roleOpt.isEmpty()) {
                return new ResponseEntity<>(new ApiResponse<>(false, "User creation failed", "Role not found"),
                        HttpStatus.BAD_REQUEST);
            }
            Role role = roleOpt.get();

            // // Convert DTO to User entity and set properties
            User userTemp = new User();
            userTemp.setEmail(userData.getEmail());
            userTemp.setFirstName(userData.getFirstName());
            userTemp.setLastName(userData.getLastName());
            userTemp.setPhone(userData.getPhone());
            userTemp.setShippingAddress(userData.getAddress());
            userTemp.setPassword(userData.getPassword());
            userTemp.setActive(userData.getActive());
            // // userTemp.setPassword(passwordEncoder.encode(userDto.getPassword())); //
            // // Encrypt password
            userTemp.setRole(role); // Attach role to user

            // // Save the user to the database
            User savedUser = userRepository.save(userTemp);

            // Create a cart for the user
            Cart cart = new Cart();
            cart.setUser(savedUser);
            // cart.setTotalPrice(BigDecimal.ZERO);

            // Set the cart in the user (bidirectional relationship)
            cartRepository.save(cart);
            savedUser.setCart(cart);

            // // Convert the saved user to UserDto (excluding password) for the response
            UserDTO responseDto = UserDTO.convertToUserDTO(savedUser);
            return new ResponseEntity<>(new ApiResponse<>(true, "User created successfully", responseDto),
                    HttpStatus.CREATED);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Error creating user", e.getMessage()));
        }
    }

    @PutMapping("users/{id}")
    public ResponseEntity<ApiResponse<UserDTO>> updateUser(@PathVariable("id") Long id,
            @RequestBody UserCreateRequestDTO userDetails) {
        try {
            Optional<User> userData = userRepository.findById(id);
            if (userData.isPresent()) {
                User userTemp = userData.get();

                // update fields
                // userTemp.setEmail(userDetails.getEmail());
                userTemp.setFirstName(userDetails.getFirstName());
                userTemp.setLastName(userDetails.getLastName());
                userTemp.setPhone(userDetails.getPhone());
                userTemp.setShippingAddress(userDetails.getAddress());
                userTemp.setActive(userDetails.getActive());

                // userTemp.setPassword(userDetails.getPassword());
                // Fetch and set Role by roleId
                if (userDetails.getRoleId() != null) {
                    Optional<Role> roleOpt = roleRepository.findById(userDetails.getRoleId());
                    if (roleOpt.isEmpty()) {
                        return new ResponseEntity<>(new ApiResponse<>(false,
                                "Role not found", null), HttpStatus.BAD_REQUEST);
                    }
                    userTemp.setRole(roleOpt.get());
                }

                // Save updated user
                User updatedUser = userRepository.save(userTemp);
                return new ResponseEntity<>(
                        new ApiResponse<>(true, "User updated successfully", UserDTO.convertToUserDTO(
                                updatedUser)),
                        HttpStatus.OK);
            }
            return new ResponseEntity<>(
                    new ApiResponse<>(false,
                            "User with id " + id
                                    + " does not exist",
                            "User not found"),
                    HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Error updating user", e.getMessage()));
        }
    }

    @DeleteMapping("users/{id}")
    public ResponseEntity<ApiResponse<String>> deleteUser(@PathVariable("id") Long id) {
        try {
            userRepository.deleteById(id);
            return ResponseEntity.noContent().build();
            // return new ResponseEntity<>(HttpStatus.NO_CONTENT);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Error deleting user", e.getMessage()));
        }
    }

    // Activate user
    @PutMapping("users/activate/{id}")
    public ResponseEntity<ApiResponse<UserDTO>> activateUser(@PathVariable("id") Long id) {
        try {
            Optional<User> userOpt = userRepository.findById(id);
            if (userOpt.isPresent()) {
                User user = userOpt.get();
                user.setActive(true);
                User updatedUser = userRepository.save(user);

                return new ResponseEntity<>(
                        new ApiResponse<>(true,
                                "User with id " + id + " is now active successfully",
                                UserDTO.convertToUserDTO(
                                        updatedUser)),
                        HttpStatus.OK);
            } else {
                return new ResponseEntity<>(
                        new ApiResponse<>(false, "User with id " + id
                                + " does not exist",
                                "User not found"),
                        HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Error deleting user", e.getMessage()));
        }

    }

    // Deactivate user
    @PutMapping("users/deactivate/{id}")
    public ResponseEntity<ApiResponse<UserDTO>> deactivateUser(@PathVariable("id") Long id) {
        Optional<User> userOpt = userRepository.findById(id);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            user.setActive(false);
            User updatedUser = userRepository.save(user);
            return new ResponseEntity<>(
                    new ApiResponse<>(true,
                            "User with ID " + id + " is now inactive successfully",
                            UserDTO.convertToUserDTO(
                                    updatedUser)),
                    HttpStatus.OK);
        } else {
            return new ResponseEntity<>(
                    new ApiResponse<>(false, "User with id " + id
                            + " does not exist",
                            "User not found"),
                    HttpStatus.NOT_FOUND);
        }
    }
}
