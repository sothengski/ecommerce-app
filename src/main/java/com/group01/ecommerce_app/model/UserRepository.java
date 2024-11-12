package com.group01.ecommerce_app.model;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    List<User> findByActiveTrue(); // Fetch only active users

    List<User> findByActiveFalse(); // Fetch only inactive users
}