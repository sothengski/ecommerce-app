package com.group01.ecommerce_app.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.group01.dto.ApiResponse;
import com.group01.dto.UserDTO;
import com.group01.dto.UserLoginDTO;
import com.group01.ecommerce_app.model.User;
import com.group01.ecommerce_app.model.UserRepository;

@CrossOrigin(origins = "http://localhost:8081")
@RestController
@RequestMapping("/api")
public class LoginController {

    @Autowired
    UserRepository userRepository;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<UserDTO>> login(@RequestBody UserLoginDTO loginRequest) {

        try {
            Optional<User> userData = userRepository.findByEmail(loginRequest.getEmail());
            if (userData.isPresent()) {
                String password = userData.get().getPassword();
                if (password.equals(loginRequest.getPassword())) {
                    return new ResponseEntity<>(new ApiResponse<>(true, "User retrieved successfully",
                            UserDTO.convertToUserDTO(userData.get())), HttpStatus.OK);
                }
                // MessageResponse msg = new MessageResponse("Incorrect password");
                return new ResponseEntity<>(new ApiResponse<>(false, "Incorrect password", null),
                        HttpStatus.FORBIDDEN);
            }
            // MessageResponse msg = new MessageResponse("No such a user");
            return new ResponseEntity<>(new ApiResponse<>(false, "No such a user", null), HttpStatus.FORBIDDEN);
        } catch (Exception e) {
            // MessageResponse msg = new MessageResponse("Server Error");
            return new ResponseEntity<>(new ApiResponse<>(false,
                    "Server Error", null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
