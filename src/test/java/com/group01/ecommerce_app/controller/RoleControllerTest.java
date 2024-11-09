// package com.group01.ecommerce_app.controller;

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
