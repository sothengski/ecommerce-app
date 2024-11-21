package com.group01.ecommerce_app.loader;

import java.io.InputStream;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.group01.ecommerce_app.model.User;

@Component
public class UserLoader implements CommandLineRunner {

    @Override
    public void run(String... args) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();

        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream("data/users.json")) {
            if (inputStream == null) {
                throw new RuntimeException("users.json file not found in resources folder.");
            }

            List<User> users = objectMapper.readValue(inputStream, new TypeReference<List<User>>() {
            });

            // Load users into database or perform operations
            for (User user : users) {
                addUserIfNotFound(user);
            }
        }
    }

    private void addUserIfNotFound(User user) {
        // Implement your logic here to check if the user exists in the database
        // and add if not found.
        // System.out.println("Adding user: " + user.getEmail() + " with role: " +
        // user.getRole());
    }
}
