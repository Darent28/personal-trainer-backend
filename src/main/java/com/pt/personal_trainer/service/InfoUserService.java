package com.pt.personal_trainer.service;

import org.springframework.stereotype.Service;

import com.pt.personal_trainer.domain.InfoUserInput;
import com.pt.personal_trainer.dto.InfoUserResponseDto;
import com.pt.personal_trainer.entity.InfoUser;
import com.pt.personal_trainer.repository.InfoUserRepository;

import jakarta.transaction.Transactional;

@Service
public class InfoUserService {

    private final InfoUserRepository infoUserRepository;

    public InfoUserService(InfoUserRepository infoUserRepository) {
        this.infoUserRepository = infoUserRepository;
    }

    @Transactional
    public InfoUserResponseDto postInfoUser(InfoUserInput infoUserInput) {

        InfoUser infoUser = new InfoUser(infoUserInput.getWheight(), infoUserInput.getHeight(), infoUserInput.getFatPorcentage(), infoUserInput.getAge(), infoUserInput.getActivityLevel(), infoUserInput.getGoal());
        infoUserRepository.save(infoUser);

        return InfoUserResponseDto.fromEntity(infoUser);
    }

}
