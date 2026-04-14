package com.pt.personal_trainer.controller;

import static org.mockito.ArgumentMatchers.any;
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
import com.pt.personal_trainer.domain.dto.DailyPlansDto;
import com.pt.personal_trainer.domain.dto.InfoUserResponseDto;
import com.pt.personal_trainer.domain.input.InfoUserInput;
import com.pt.personal_trainer.exception.CustomExceptions.NotFoundException;
import com.pt.personal_trainer.service.InfoUserService;

@WebMvcTest(InfoController.class)
@AutoConfigureMockMvc(addFilters = false)
class InfoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @MockitoBean
    private InfoUserService infoUserService;

    @MockitoBean
    private JwtUtil jwtUtil;

    @MockitoBean
    private JwtAuthFilter jwtAuthFilter;

    @MockitoBean
    private UserDetailsServiceImpl userDetailsService;

    @Test
    void getInfoUsersByUserId_shouldReturnList() throws Exception {
        InfoUserResponseDto dto = new InfoUserResponseDto(1L, 80.0, 180.0, 15.0, 25, 3, 1, 1L);
        when(infoUserService.getInfoUsersByUserId(1L)).thenReturn(List.of(dto));

        mockMvc.perform(get("/api/info/user/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].weight").value(80.0))
            .andExpect(jsonPath("$[0].height").value(180.0));
    }

    @Test
    void getInfoUserById_shouldReturnInfo() throws Exception {
        InfoUserResponseDto dto = new InfoUserResponseDto(1L, 80.0, 180.0, 15.0, 25, 3, 1, 1L);
        when(infoUserService.getInfoUserById(1L)).thenReturn(dto);

        mockMvc.perform(get("/api/info/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.weight").value(80.0));
    }

    @Test
    void getInfoUserById_shouldReturn404_whenNotFound() throws Exception {
        when(infoUserService.getInfoUserById(99L))
            .thenThrow(new NotFoundException("User info not found with id: 99"));

        mockMvc.perform(get("/api/info/99"))
            .andExpect(status().isNotFound());
    }

    @Test
    void postInfoUser_shouldReturn201() throws Exception {
        InfoUserInput input = new InfoUserInput(80.0, 180.0, 15.0, 1L, 3, 1);
        InfoUserResponseDto dto = new InfoUserResponseDto(1L, 80.0, 180.0, 15.0, 25, 3, 1, 1L);
        when(infoUserService.postInfoUser(any(InfoUserInput.class))).thenReturn(dto);

        mockMvc.perform(post("/api/info")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(input)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.weight").value(80.0));
    }

    @Test
    void postInfoUser_shouldReturn400_whenInvalidInput() throws Exception {
        InfoUserInput input = new InfoUserInput(null, null, null, null, null, null);

        mockMvc.perform(post("/api/info")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(input)))
            .andExpect(status().isBadRequest());
    }

    @Test
    void getDailyPlanByInfoId_shouldReturnPlan() throws Exception {
        DailyPlansDto dto = new DailyPlansDto(1L, 2000, 150, 70, 200, 1L);
        when(infoUserService.getDailyPlanByInfoId(1L)).thenReturn(dto);

        mockMvc.perform(get("/api/info/1/daily-plan"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.totalCalories").value(2000))
            .andExpect(jsonPath("$.totalProteins").value(150));
    }
}
