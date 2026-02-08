package com.example.userauthservice.controller;

import com.example.userauthservice.dto.UserDto;
import com.example.userauthservice.models.Role;
import com.example.userauthservice.models.User;
import com.example.userauthservice.services.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserService userService;

    @Test
    void getUserById_returnsUserDto() throws Exception {
        User user = new User();
        Role role = new Role();
        role.setValue("ROLE_USER");
        user.setId(1L);
        user.setName("Test User");
        user.setEmail("test@example.com");
        user.setRoles(Arrays.asList(role));

        when(userService.getUserById(eq(1L))).thenReturn(user);

        mockMvc.perform(get("/users/{id}", 1L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Test User"))
                .andExpect(jsonPath("$.email").value("test@example.com"))
                .andExpect(jsonPath("$.roles[0]").value("ROLE_USER"));
    }

    @Test
    void createUser_returnsCreatedUserDto() throws Exception {
        UserDto request = new UserDto();
        request.setName("New User");
        request.setEmail("new@example.com");
        Role requestRole = new Role();
        requestRole.setValue("ROLE_USER");
        request.setRoles(Arrays.asList(requestRole));

        User created = new User();
        Role role = new Role();
        role.setValue("ROLE_USER");
        created.setId(2L);
        created.setName("New User");
        created.setEmail("new@example.com");
        created.setRoles(Arrays.asList(role));

        when(userService.createUser(any(User.class))).thenReturn(created);

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(2))
                .andExpect(jsonPath("$.name").value("New User"))
                .andExpect(jsonPath("$.email").value("new@example.com"))
                .andExpect(jsonPath("$.roles[0]").value("ROLE_USER"));
    }
}

