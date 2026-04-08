package com.pt.personal_trainer.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pt.personal_trainer.auth.JwtAuthFilter;
import com.pt.personal_trainer.auth.JwtUtil;
import com.pt.personal_trainer.auth.UserDetailsServiceImpl;
import com.pt.personal_trainer.domain.dto.UserResponseDto;
import com.pt.personal_trainer.domain.input.UserInput;
import com.pt.personal_trainer.exception.CustomExceptions.NotFoundException;
import com.pt.personal_trainer.service.UserService;

@WebMvcTest(UserController.class)
@AutoConfigureMockMvc(addFilters = false)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private JwtUtil jwtUtil;

    @MockitoBean
    private JwtAuthFilter jwtAuthFilter;

    @MockitoBean
    private UserDetailsServiceImpl userDetailsService;

    @Test
    void getUsers_shouldReturnList() throws Exception {
        UserResponseDto dto = new UserResponseDto(1L, "john", "john@test.com", 1, false);
        when(userService.getUsers()).thenReturn(List.of(dto));

        mockMvc.perform(get("/api/users"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].username").value("john"))
            .andExpect(jsonPath("$[0].email").value("john@test.com"));
    }

    @Test
    void getUserById_shouldReturnUser() throws Exception {
        UserResponseDto dto = new UserResponseDto(1L, "john", "john@test.com", 1, false);
        when(userService.getUserById(1L)).thenReturn(dto);

        mockMvc.perform(get("/api/users/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.username").value("john"));
    }

    @Test
    void getUserById_shouldReturn404_whenNotFound() throws Exception {
        when(userService.getUserById(99L)).thenThrow(new NotFoundException("User not found with id: 99"));

        mockMvc.perform(get("/api/users/99"))
            .andExpect(status().isNotFound());
    }

    @Test
    void putUserById_shouldUpdateUser() throws Exception {
        UserInput input = new UserInput("newname", "john@test.com", "password123", 1);
        UserResponseDto dto = new UserResponseDto(1L, "newname", "john@test.com", 1, false);
        when(userService.updateUsername(eq(1L), any(UserInput.class))).thenReturn(dto);

        mockMvc.perform(put("/api/users/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(input)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.username").value("newname"));
    }

    @Test
    void deleteUser_shouldSoftDelete() throws Exception {
        UserResponseDto dto = new UserResponseDto(1L, "john", "john@test.com", 1, false);
        when(userService.deleteUser(1L)).thenReturn(dto);

        mockMvc.perform(delete("/api/users/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.username").value("john"));
    }
}
