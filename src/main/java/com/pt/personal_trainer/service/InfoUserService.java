package com.pt.personal_trainer.service;

import org.springframework.stereotype.Service;

import com.pt.personal_trainer.domain.dto.InfoUserResponseDto;
import com.pt.personal_trainer.domain.input.InfoUserInput;
import com.pt.personal_trainer.entity.DailyPlans;
import com.pt.personal_trainer.entity.InfoUser;
import com.pt.personal_trainer.exception.CustomExceptions.NotFoundException;
import com.pt.personal_trainer.repository.DailyPlansRepository;
import com.pt.personal_trainer.repository.InfoUserRepository;
import com.pt.personal_trainer.repository.LevelActivityTypeRepository;
import com.pt.personal_trainer.repository.UserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class InfoUserService {

    private final UserRepository userRepository;
    private final InfoUserRepository infoUserRepository;
    private final LevelActivityTypeRepository levelActivityTypeRepository;
    private final DailyPlansRepository dailyPlansRepository;

    @Transactional
    public InfoUserResponseDto postInfoUser(InfoUserInput input) {
        InfoUser infoUser = InfoUser.builder()
            .wheight(input.getWheight())
            .height(input.getHeight())
            .fatPorcentage(input.getFatPorcentage())
            .age(input.getAge())
            .activityLevel(input.getActivityLevel())
            .goal(input.getGoal())
            .userId(input.getUserId())
            .build();
        infoUserRepository.save(infoUser);

        dailyPlansRepository.save(calculateMacros(input, infoUser.getId()));

        return InfoUserResponseDto.fromEntity(infoUser);
    }

    private DailyPlans calculateMacros(InfoUserInput input, Long userInfoId) {
        int genderIdUser = userRepository.findGenderIdById(input.getUserId())
            .orElseThrow(() -> new NotFoundException("User not found with id: " + input.getUserId()));

        double base = 10 * input.getWheight() + 6.25 * input.getHeight() - 5 * input.getAge();
        int bmr = switch (genderIdUser) {
            case 1 -> (int) (base + 5);    // Male
            case 2 -> (int) (base - 161);  // Female
            default -> throw new IllegalArgumentException("Invalid gender: " + genderIdUser);
        };

        double factor = levelActivityTypeRepository.findFactorById(input.getActivityLevel());
        int tdee = (int) (bmr * factor);

        record GoalConfig(int calorieOffset, double proteinPerKg, double fatPerKg) {}

        // 1 Definicion, 2 Volumen limpio, 3 Recomposicion
        GoalConfig config = switch (input.getGoal()) {
            case 1 -> new GoalConfig(-400, 2.2, 0.8);
            case 2 -> new GoalConfig(+300, 1.6, 0.6);
            case 3 -> new GoalConfig(   0, 2.0, 0.7);
            default -> throw new IllegalArgumentException("Invalid goal: " + input.getGoal());
        };

        int calories = tdee + config.calorieOffset();
        int proteins = (int) (input.getWheight() * config.proteinPerKg());
        int fats     = (int) (input.getWheight() * config.fatPerKg());
        int carbohydrates = (calories - (proteins * 4) - (fats * 9)) / 4;

        return DailyPlans.builder()
            .totalCalories(calories)
            .totalProteins(proteins)
            .totalFats(fats)
            .totalCarbs(carbohydrates)
            .userInfoId(userInfoId)
            .build();
    }

}