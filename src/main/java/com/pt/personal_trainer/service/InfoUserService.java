package com.pt.personal_trainer.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

import com.pt.personal_trainer.domain.dto.DailyPlansDto;
import com.pt.personal_trainer.domain.dto.InfoUserResponseDto;
import com.pt.personal_trainer.domain.input.InfoUserInput;
import com.pt.personal_trainer.entity.DailyPlans;
import com.pt.personal_trainer.entity.GoalMacroConfig;
import com.pt.personal_trainer.entity.InfoUser;
import com.pt.personal_trainer.exception.CustomExceptions.NotFoundException;
import com.pt.personal_trainer.exception.CustomExceptions.ProcessServiceException;
import com.pt.personal_trainer.exception.CustomExceptions.ServerErrorException;
import com.pt.personal_trainer.repository.DailyPlansRepository;
import com.pt.personal_trainer.repository.GoalMacroConfigRepository;
import com.pt.personal_trainer.repository.InfoUserRepository;
import com.pt.personal_trainer.repository.LevelActivityTypeRepository;
import com.pt.personal_trainer.repository.UserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class InfoUserService {

    private static final Logger log = LoggerFactory.getLogger(InfoUserService.class);

    private final UserRepository userRepository;
    private final InfoUserRepository infoUserRepository;
    private final LevelActivityTypeRepository levelActivityTypeRepository;
    private final DailyPlansRepository dailyPlansRepository;
    private final GoalMacroConfigRepository goalMacroConfigRepository;

    public DailyPlansDto getDailyPlanByInfoId(Long infoId) {
        DailyPlans plan = dailyPlansRepository.findByUserInfoId(infoId)
            .orElseThrow(() -> new NotFoundException("Daily plan not found for user info id: " + infoId));
        return DailyPlansDto.fromEntity(plan);
    }

    public List<InfoUserResponseDto> getInfoUsersByUserId(Long userId) {
        try {
            return infoUserRepository.findByUserId(userId).stream()
                .map(InfoUserResponseDto::fromEntity)
                .toList();
        } catch (Exception e) {
            log.error("Failed to retrieve info list for userId={}", userId, e);
            throw new ServerErrorException("Failed to retrieve user info list. Please try again later.");
        }
    }

    public InfoUserResponseDto getInfoUserById(Long id) {
        InfoUser infoUser = infoUserRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("User info not found with id: " + id));
        return InfoUserResponseDto.fromEntity(infoUser);
    }

    @Transactional
    public InfoUserResponseDto 
    postInfoUser(InfoUserInput input) {
        try {
            InfoUser infoUser = InfoUser.builder()
                .weight(input.getWeight())
                .height(input.getHeight())
                .fatPercentage(input.getFatPercentage())
                .age(input.getAge())
                .activityLevel(input.getActivityLevel())
                .goal(input.getGoal())
                .userId(input.getUserId())
                .build();
            infoUserRepository.save(infoUser);

            dailyPlansRepository.save(calculateMacros(input, infoUser.getId()));

            return InfoUserResponseDto.fromEntity(infoUser);
        } catch (NotFoundException | ProcessServiceException e) {
            throw e;
        } catch (Exception e) {
            log.error("Failed to create user info", e);
            throw new ServerErrorException("Failed to create user info. Please try again later.");
        }
    }

    /**
     * Calculates daily macro targets intelligently.
     *
     * Ranges for calories, protein and fat are loaded from the DB (goal_macro_config table).
     * The adjustment factor (0.0–1.0) is derived from body-fat percentage (clamped at 30%).
     * Calories use t² for a progressive curve: conservative at low fat%, ramps up at high fat%.
     */
    private DailyPlans calculateMacros(InfoUserInput input, Long userInfoId) {
        // --- BMR (Harris-Benedict) ---
        int genderIdUser = userRepository.findGenderIdById(input.getUserId())
            .orElseThrow(() -> new NotFoundException("User not found with id: " + input.getUserId()));

        double base = 10 * input.getWeight() + 6.25 * input.getHeight() - 5 * input.getAge();
        int bmr = switch (genderIdUser) {
            case 1 -> (int) (base + 5);
            case 2 -> (int) (base - 161);
            default -> throw new ProcessServiceException("Invalid gender id: " + genderIdUser);
        };

        // --- TDEE ---
        Double factor = levelActivityTypeRepository.findFactorById(input.getActivityLevel());
        if (factor == null) {
            throw new NotFoundException("Activity level not found with id: " + input.getActivityLevel());
        }
        int tdee = (int) (bmr * factor);

        // --- Goal config from DB ---
        GoalMacroConfig config = goalMacroConfigRepository.findByGoalTypeId(input.getGoal())
            .orElseThrow(() -> new NotFoundException("Goal macro config not found for goal id: " + input.getGoal()));

        // t: 0.0 (lean) → 1.0 (high fat%), clamped. Reference: 30% body fat = upper bound.
        double t = Math.max(0.0, Math.min(1.0, input.getFatPercentage() / 30.0));

        // Calories use t² for a progressive curve: conservative at low fat%, ramps up quickly at high fat%
        double calorieFactor = t * t;
        // Protein and fat scale linearly with fat%
        double macroFactor   = t;

        int calorieOffset   = interpolate(config.getCalorieOffsetMin(), config.getCalorieOffsetMax(), calorieFactor);
        double proteinPerKg = interpolate(config.getProteinPerKgMin(), config.getProteinPerKgMax(), macroFactor);
        double fatPerKg     = interpolate(config.getFatPerKgMin(), config.getFatPerKgMax(), 0.5);

        int calories = tdee + calorieOffset;
        int proteins = (int) (input.getWeight() * proteinPerKg);
        int fats     = (int) (input.getWeight() * fatPerKg);
        int carbs    = (calories - (proteins * 4) - (fats * 9)) / 4;

        return DailyPlans.builder()
            .totalCalories(calories)
            .totalProteins(proteins)
            .totalFats(fats)
            .totalCarbs(carbs)
            .userInfoId(userInfoId)
            .build();
    }

    private int interpolate(int min, int max, double t) {
        return (int) Math.round(min + (max - min) * t);
    }

    private double interpolate(double min, double max, double t) {
        return min + (max - min) * t;
    }

}
