package com.pt.personal_trainer.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.pt.personal_trainer.auth.JwtAuthFilter;
import com.pt.personal_trainer.auth.JwtUtil;
import com.pt.personal_trainer.auth.UserDetailsServiceImpl;
import com.pt.personal_trainer.entity.GoalType;
import com.pt.personal_trainer.entity.LevelActivityType;
import com.pt.personal_trainer.repository.GoalTypeRepository;
import com.pt.personal_trainer.repository.LevelActivityTypeRepository;

@WebMvcTest(LookupController.class)
@AutoConfigureMockMvc(addFilters = false)
class LookupControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private GoalTypeRepository goalTypeRepository;

    @MockitoBean
    private LevelActivityTypeRepository levelActivityTypeRepository;

    @MockitoBean
    private JwtUtil jwtUtil;

    @MockitoBean
    private JwtAuthFilter jwtAuthFilter;

    @MockitoBean
    private UserDetailsServiceImpl userDetailsService;

    @Test
    void getGoalTypes_shouldReturnList() throws Exception {
        GoalType goal = new GoalType(1, "Cutting", "Lose weight");
        when(goalTypeRepository.findAll()).thenReturn(List.of(goal));

        mockMvc.perform(get("/api/lookup/goal-types"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].goalName").value("Cutting"))
            .andExpect(jsonPath("$[0].goalDescription").value("Lose weight"));
    }

    @Test
    void getActivityLevels_shouldReturnList() throws Exception {
        LevelActivityType level = new LevelActivityType(1, "Sedentary", 1.2);
        when(levelActivityTypeRepository.findAll()).thenReturn(List.of(level));

        mockMvc.perform(get("/api/lookup/activity-levels"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].activityName").value("Sedentary"))
            .andExpect(jsonPath("$[0].factor").value(1.2));
    }
}
