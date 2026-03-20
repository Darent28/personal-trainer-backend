package com.pt.personal_trainer.service;

import org.springframework.stereotype.Service;

import com.pt.personal_trainer.domain.InfoUserInput;
import com.pt.personal_trainer.dto.InfoUserResponseDto;
import com.pt.personal_trainer.entity.GoalType;
import com.pt.personal_trainer.entity.InfoUser;
import com.pt.personal_trainer.exception.CustomExceptions.NotFoundException;
import com.pt.personal_trainer.repository.InfoUserRepository;
import com.pt.personal_trainer.repository.GoalTypeRepository;

import jakarta.transaction.Transactional;

@Service
public class InfoUserService {

    private final InfoUserRepository infoUserRepository;
    private final GoalTypeRepository goalTypeRepository;

    public InfoUserService(InfoUserRepository infoUserRepository, GoalTypeRepository goalTypeRepository) {
        this.infoUserRepository = infoUserRepository;
        this.goalTypeRepository = goalTypeRepository;
    }

    @Transactional
    public InfoUserResponseDto postInfoUser(InfoUserInput infoUserInput) {

        GoalType goalType = goalTypeRepository.findById(infoUserInput.getGoal())
                .orElseThrow(() -> new NotFoundException("Goal type not found with id: " + infoUserInput.getGoal()));

        InfoUser infoUser = new InfoUser(infoUserInput.getWheight(), infoUserInput.getHeight(), infoUserInput.getFatPorcentage(), infoUserInput.getAge(), infoUserInput.getActivityLevel(), goalType);
        infoUserRepository.save(infoUser);

        return InfoUserResponseDto.fromEntity(infoUser);
    }

}
