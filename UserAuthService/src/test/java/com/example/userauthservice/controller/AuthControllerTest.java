// java
package com.example.userauthservice.controller;

import com.example.userauthservice.dto.LoginRequestDto;
import com.example.userauthservice.dto.SignupRequestDto;
import com.example.userauthservice.dto.ValidateTokenDto;
import com.example.userauthservice.models.Role;
import com.example.userauthservice.models.User;
import com.example.userauthservice.services.IAuthService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.antlr.v4.runtime.misc.Pair;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthController.class)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private IAuthService authService;

    @Test
    void signup_returnsCreatedAndUserDto() throws Exception {
        SignupRequestDto req = new SignupRequestDto();
        req.setName("Alice");
        req.setEmail("alice@example.com");
        req.setPassword("password123");

        User user = new User();
        Role role = new Role();
        role.setValue("ROLE_USER");
        user.setId(10L);
        user.setName("Alice");
        user.setEmail("alice@example.com");
        user.setRoles(Arrays.asList(role));

        when(authService.signup(eq("Alice"), eq("alice@example.com"), eq("password123"))).thenReturn(user);

        mockMvc.perform(post("/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(10))
                .andExpect(jsonPath("$.name").value("Alice"))
                .andExpect(jsonPath("$.email").value("alice@example.com"))
                .andExpect(jsonPath("$.roles[0]").value("ROLE_USER"));
    }

    @Test
    void login_returnsOk_withSetCookieHeader_andUserDto() throws Exception {
        LoginRequestDto req = new LoginRequestDto();
        req.setEmail("bob@example.com");
        req.setPassword("secret");

        User user = new User();
        Role role1 = new Role();
        role1.setValue("ROLE_USER");
        Role role2 = new Role();
        role2.setValue("ROLE_ADMIN");
        user.setId(20L);
        user.setName("Bob");
        user.setEmail("bob@example.com");
        user.setRoles(Arrays.asList(role1, role2));

        String cookieValue = "SESSION=token123; HttpOnly; Path=/";

        when(authService.login(eq("bob@example.com"), eq("secret"))).thenReturn(new Pair<>(user, cookieValue));

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(header().stringValues("Set-Cookie", cookieValue))
                .andExpect(jsonPath("$.id").value(20))
                .andExpect(jsonPath("$.name").value("Bob"))
                .andExpect(jsonPath("$.email").value("bob@example.com"))
                .andExpect(jsonPath("$.roles[0]").value("ROLE_USER"))
                .andExpect(jsonPath("$.roles[1]").value("ROLE_ADMIN"));
    }

    @Test
    void validateToken_returnsOk_whenTokenValid() throws Exception {
        ValidateTokenDto dto = new ValidateTokenDto();
        dto.setToken("valid-token");

        when(authService.validateToken(eq("valid-token"))).thenReturn(Boolean.TRUE);

        mockMvc.perform(post("/auth/validateToken")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk());
    }

    @Test
    void validateToken_returnsBadRequest_whenTokenInvalid() throws Exception {
        ValidateTokenDto dto = new ValidateTokenDto();
        dto.setToken("invalid-token");

        when(authService.validateToken(eq("invalid-token"))).thenReturn(Boolean.FALSE);

        mockMvc.perform(post("/auth/validateToken")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }
}

