package com.group01.ecommerce_app.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
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

import com.group01.ecommerce_app.model.Role;
import com.group01.ecommerce_app.model.RoleRepository;

@CrossOrigin(origins = "http://localhost:8081")
@RestController
@RequestMapping("/api")
public class RoleController {

    @Autowired
    private RoleRepository roleRepository;

    // Constructor injection instead of Autowired
    // private final RoleRepository roleRepository;

    // public RoleController(RoleRepository roleRepository) {
    // this.roleRepository = roleRepository;
    // }

    // Get all roles
    @GetMapping("/roles")
    public ResponseEntity<List<Role>> getAllRoles() {
        try {
            List<Role> roles = roleRepository.findAll();

            // List of roles is Empty
            if (roles.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            // List of roles have the data
            return ResponseEntity.ok(roles);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Get role by ID
    @GetMapping("/roles/{id}")
    public ResponseEntity<Role> getRoleById(@PathVariable("id") int id) {
        try {
            Optional<Role> roleData = roleRepository.findById(id);
            if (roleData.isPresent()) {
                Role roleTemp = roleData.get();
                return new ResponseEntity<>(roleTemp, HttpStatus.OK);
            }
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            // return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Get all users by roleID
    // @GetMapping("/roles/{id}/users")

    // Create a new role
    @PostMapping("/roles")
    public ResponseEntity<Role> createRole(@RequestBody Role role) {
        try {
            Role roleTemp = roleRepository.save(new Role(role.getName()));
            return ResponseEntity.ok(roleTemp);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Update an existing role
    @PutMapping("/roles/{id}")
    public ResponseEntity<Role> updateRole(@PathVariable("id") int id, @RequestBody Role updateRole) {
        try {
            Optional<Role> roleData = roleRepository.findById(id);
            if (roleData.isPresent()) {
                Role roleTemp = roleData.get();
                roleTemp.setName(updateRole.getName());
                roleRepository.save(roleTemp);
                return new ResponseEntity<>(roleTemp, HttpStatus.OK);
            }
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

            //// Lamda Function
            // return roleRepository.findById(id)
            // .map(role -> {
            // role.setName(updateRole.getName());
            // roleRepository.save(role);
            // return new ResponseEntity<>(role, HttpStatus.OK);
            // })
            // .orElseGet((() -> new ResponseEntity<>(HttpStatus.NOT_FOUND)));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Delete a role
    @DeleteMapping("/roles/{id}")
    public ResponseEntity<Void> deleteRole(@PathVariable("id") int id) {
        try {
            roleRepository.deleteById(id);
            return ResponseEntity.noContent().build();
            // return new ResponseEntity<>(HttpStatus.NO_CONTENT);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
