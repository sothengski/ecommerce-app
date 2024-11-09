package com.group01.ecommerce_app.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.group01.ecommerce_app.model.Role;
import com.group01.ecommerce_app.model.RoleRepository;

class RoleControllerTest {

    private MockMvc mockMvc;

    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private RoleController roleController;

    // @Autowired
    // private ObjectMapper objectMapper;
    private final ObjectMapper objectMapper = new ObjectMapper(); // Initialize ObjectMapper here

    Role role;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(roleController).build();

        // Create a new role as admin
        role = Role.builder().id(1).name("Admin").build();
    }

    @Test
    void testGetAllRoles() throws Exception {
        System.out.println("Running testGetAllRoles..."); // Add this line for debug

        // Precondition
        List<Role> roles = new ArrayList<>();
        roles.add(role); // roles.add(new Role("Admin"));
        roles.add(new Role(2, "User"));

        when(roleRepository.findAll()).thenReturn(roles);

        // action
        ResultActions response = mockMvc.perform(get("/api/roles"));

        // verify the output
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.size()").value(roles.size()));

        // mockMvc.perform(get("/api/roles"));
        // .andExpect(status().isOk())
        // .andExpect(jsonPath("$.size()").value(2));

        System.out.println("testGetAllRoles completed."); // Add this line for debug

    }

    @Test
    void testGetRoleById() throws Exception {
        System.out.println("Running testGetRoleById..."); // Add this line for debug

        // // Arrange
        Role role = new Role("Admin");
        when(roleRepository.findById(1)).thenReturn(Optional.of(role));

        // // Act & Assert
        mockMvc.perform(get("/api/roles/1"))
                .andExpect(status().isOk())
                // .andExpect(jsonPath("$.roles").exists())
                .andExpect(jsonPath("$.id").value(role.getId()))
                .andExpect(jsonPath("$.name").value(role.getName()));// .value("Admin"));

        verify(roleRepository, times(1)).findById(1);

        System.out.println("testGetRoleById completed."); // Add this line for debug

    }

    @Test
    void testCreateRole() throws Exception {
        System.out.println("Running testCreateRole..."); // Add this line for debug

        Role role = new Role("Admin");
        // Precondition
        when(roleRepository.save(any(Role.class))).thenReturn(role);

        // action
        ResultActions response = mockMvc.perform(post("/api/roles")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(role)));

        // verify
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.name").value("Admin"));

        // mockMvc.perform(post("/api/roles")
        // .contentType(MediaType.APPLICATION_JSON)
        // .content(objectMapper.writeValueAsString(role)))
        // .andExpect(status().isOk())
        // .andExpect(jsonPath("$.name").value("Admin"));

        System.out.println("testCreateRole completed."); // Add this line for debug

    }

    @Test
    void testUpdateRole() throws Exception {
        System.out.println("Running testUpdateRole..."); // Add this line for debug

        Role role = new Role("Admin");
        Role updatedRole = new Role("Manager");

        when(roleRepository.findById(1)).thenReturn(Optional.of(role));
        when(roleRepository.save(any(Role.class))).thenReturn(updatedRole);

        mockMvc.perform(put("/api/roles/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedRole)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Manager"));

        System.out.println("testUpdateRole completed."); // Add this line for debug

    }

    @Test
    void testDeleteRole() throws Exception {
        System.out.println("Running testDeleteRole..."); // Add this line for debug

        doNothing().when(roleRepository).deleteById(1);

        mockMvc.perform(delete("/api/roles/1"))
                .andExpect(status().isNoContent());

        System.out.println("testDeleteRole completed."); // Add this line for debug

    }
}

// import java.util.Optional;

// import org.junit.jupiter.api.Test;
// import static org.mockito.ArgumentMatchers.any;
// import static org.mockito.Mockito.times;
// import static org.mockito.Mockito.verify;
// import static org.mockito.Mockito.when;
// import org.springframework.beans.factory.annotation.Autowired;
// import
// org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
// import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
// import org.springframework.boot.test.mock.mockito.MockBean;
// import org.springframework.http.MediaType;
// import org.springframework.test.web.servlet.MockMvc;
// import static
// org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
// import static
// org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
// import static
// org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
// import static
// org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// import com.fasterxml.jackson.databind.ObjectMapper;
// import com.group01.ecommerce_app.model.Role;
// import com.group01.ecommerce_app.model.RoleRepository;

// @WebMvcTest(RoleController.class)
// @AutoConfigureMockMvc(addFilters = false) // Disable security filters for
// tests
// public class RoleControllerTest {

// @Autowired
// private MockMvc mockMvc;

// @MockBean
// private RoleRepository roleRepository;

// @Autowired
// private ObjectMapper objectMapper;

// @Test
// public void getRoleById_Found() throws Exception {
// // Arrange
// Role role = new Role(1, "Admin");
// when(roleRepository.findById(1)).thenReturn(Optional.of(role));

// // Act & Assert
// mockMvc.perform(get("/api/roles/1"))
// .andExpect(status().isOk())
// .andExpect(jsonPath("$.id").value(role.getId()))
// .andExpect(jsonPath("$.name").value(role.getName()));

// verify(roleRepository, times(1)).findById(1);
// }

// @Test
// public void getRoleById_NotFound() throws Exception {
// // Arrange
// when(roleRepository.findById(1)).thenReturn(Optional.empty());

// // Act & Assert
// mockMvc.perform(get("/api/roles/1"))
// .andExpect(status().isNotFound());

// verify(roleRepository, times(1)).findById(1);
// }

// @Test
// public void updateRole_Found() throws Exception {
// // Arrange
// Role existingRole = new Role(1, "User");
// Role updatedRole = new Role();
// updatedRole.setName("Admin");

// when(roleRepository.findById(1)).thenReturn(Optional.of(existingRole));
// when(roleRepository.save(any(Role.class))).thenReturn(existingRole);

// // Act & Assert
// mockMvc.perform(put("/api/roles/1")
// .contentType(MediaType.APPLICATION_JSON)
// .content(objectMapper.writeValueAsString(updatedRole)))
// .andExpect(status().isOk())
// .andExpect(jsonPath("$.id").value(existingRole.getId()))
// .andExpect(jsonPath("$.name").value("Admin"));

// verify(roleRepository, times(1)).findById(1);
// verify(roleRepository, times(1)).save(existingRole);
// }

// @Test
// public void updateRole_NotFound() throws Exception {
// // Arrange
// Role updatedRole = new Role();
// updatedRole.setName("Admin");

// when(roleRepository.findById(1)).thenReturn(Optional.empty());

// // Act & Assert
// mockMvc.perform(put("/api/roles/1")
// .contentType(MediaType.APPLICATION_JSON)
// .content(objectMapper.writeValueAsString(updatedRole)))
// .andExpect(status().isNotFound());

// verify(roleRepository, times(1)).findById(1);
// verify(roleRepository, times(0)).save(any(Role.class));
// }
// }
