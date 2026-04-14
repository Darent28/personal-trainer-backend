package com.pt.personal_trainer.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.pt.personal_trainer.domain.dto.DailyPlansDto;
import com.pt.personal_trainer.domain.dto.InfoUserResponseDto;
import com.pt.personal_trainer.domain.input.InfoUserInput;
import com.pt.personal_trainer.entity.DailyPlans;
import com.pt.personal_trainer.entity.GoalMacroConfig;
import com.pt.personal_trainer.entity.InfoUser;
import com.pt.personal_trainer.entity.User;
import com.pt.personal_trainer.exception.CustomExceptions.NotFoundException;
import com.pt.personal_trainer.repository.DailyPlansRepository;
import com.pt.personal_trainer.repository.GoalMacroConfigRepository;
import com.pt.personal_trainer.repository.InfoUserRepository;
import com.pt.personal_trainer.repository.LevelActivityTypeRepository;
import com.pt.personal_trainer.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
class InfoUserServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private InfoUserRepository infoUserRepository;
    @Mock
    private LevelActivityTypeRepository levelActivityTypeRepository;
    @Mock
    private DailyPlansRepository dailyPlansRepository;
    @Mock
    private GoalMacroConfigRepository goalMacroConfigRepository;

    @InjectMocks
    private InfoUserService infoUserService;

    @Test
    void getDailyPlanByInfoId_shouldReturnPlan() {
        DailyPlans plan = new DailyPlans(2000, 150, 70, 200, 1L);
        plan.setId(1L);
        when(dailyPlansRepository.findByUserInfoId(1L)).thenReturn(Optional.of(plan));

        DailyPlansDto result = infoUserService.getDailyPlanByInfoId(1L);

        assertThat(result.totalCalories()).isEqualTo(2000);
        assertThat(result.totalProteins()).isEqualTo(150);
    }

    @Test
    void getDailyPlanByInfoId_shouldThrow_whenNotFound() {
        when(dailyPlansRepository.findByUserInfoId(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> infoUserService.getDailyPlanByInfoId(99L))
            .isInstanceOf(NotFoundException.class);
    }

    @Test
    void getInfoUsersByUserId_shouldReturnList() {
        InfoUser info = new InfoUser(80.0, 180.0, 15.0, 25, 3, 1, 1L);
        info.setId(1L);
        when(infoUserRepository.findByUserId(1L)).thenReturn(List.of(info));

        List<InfoUserResponseDto> result = infoUserService.getInfoUsersByUserId(1L);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).weight()).isEqualTo(80.0);
    }

    @Test
    void getInfoUserById_shouldReturnInfo() {
        InfoUser info = new InfoUser(80.0, 180.0, 15.0, 25, 3, 1, 1L);
        info.setId(1L);
        when(infoUserRepository.findById(1L)).thenReturn(Optional.of(info));

        InfoUserResponseDto result = infoUserService.getInfoUserById(1L);

        assertThat(result.weight()).isEqualTo(80.0);
        assertThat(result.height()).isEqualTo(180.0);
    }

    @Test
    void getInfoUserById_shouldThrow_whenNotFound() {
        when(infoUserRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> infoUserService.getInfoUserById(99L))
            .isInstanceOf(NotFoundException.class);
    }

    @Test
    void postInfoUser_shouldCalculateMacrosAndSave() {
        InfoUserInput input = new InfoUserInput(80.0, 180.0, 15.0, 1L, 3, 1);

        User user = new User("john", "john@test.com", "enc", 1, LocalDate.of(2000, 1, 15));
        user.setId(1L);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(infoUserRepository.save(any(InfoUser.class))).thenAnswer(inv -> {
            InfoUser u = inv.getArgument(0);
            u.setId(10L);
            return u;
        });
        when(userRepository.findGenderIdById(1L)).thenReturn(Optional.of(1));
        when(levelActivityTypeRepository.findFactorById(3)).thenReturn(1.55);
        GoalMacroConfig config = new GoalMacroConfig(1, 1, -500, -200, 2.0, 2.5, 0.8, 1.2);
        when(goalMacroConfigRepository.findByGoalTypeId(1)).thenReturn(Optional.of(config));
        when(dailyPlansRepository.save(any(DailyPlans.class))).thenAnswer(inv -> inv.getArgument(0));

        InfoUserResponseDto result = infoUserService.postInfoUser(input);

        assertThat(result.weight()).isEqualTo(80.0);
        verify(dailyPlansRepository).save(any(DailyPlans.class));
    }

    @Test
    void postInfoUser_shouldThrow_whenUserNotFound() {
        InfoUserInput input = new InfoUserInput(80.0, 180.0, 15.0, 99L, 3, 1);

        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> infoUserService.postInfoUser(input))
            .isInstanceOf(NotFoundException.class);
    }

    @Test
    void postInfoUser_shouldThrow_whenActivityLevelNotFound() {
        InfoUserInput input = new InfoUserInput(80.0, 180.0, 15.0, 1L, 99, 1);

        User user = new User("john", "john@test.com", "enc", 1, LocalDate.of(2000, 1, 15));
        user.setId(1L);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(infoUserRepository.save(any(InfoUser.class))).thenAnswer(inv -> {
            InfoUser u = inv.getArgument(0);
            u.setId(10L);
            return u;
        });
        when(userRepository.findGenderIdById(1L)).thenReturn(Optional.of(1));
        when(levelActivityTypeRepository.findFactorById(99)).thenReturn(null);

        assertThatThrownBy(() -> infoUserService.postInfoUser(input))
            .isInstanceOf(NotFoundException.class);
    }
}
