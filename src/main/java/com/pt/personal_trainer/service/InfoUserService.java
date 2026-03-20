package com.pt.personal_trainer.service;

import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import com.pt.personal_trainer.domain.DietInput;
import com.pt.personal_trainer.domain.InfoUserInput;
import com.pt.personal_trainer.dto.InfoUserResponseDto;
import com.pt.personal_trainer.entity.InfoUser;
import com.pt.personal_trainer.repository.InfoUserRepository;

import jakarta.transaction.Transactional;

@Service
public class InfoUserService {

    private final InfoUserRepository infoUserRepository;

    private final UserRepository userRepository;

    public InfoUserService(InfoUserRepository infoUserRepository, UserRepository userRepository) {
        this.infoUserRepository = infoUserRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public InfoUserResponseDto postInfoUser(InfoUserInput infoUserInput) {

        if(infoUserInput != null) {
            getMacros(infoUserInput);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid input data");
        }

        InfoUser infoUser = new InfoUser(infoUserInput.getWheight(), infoUserInput.getHeight(), infoUserInput.getFatPorcentage(), infoUserInput.getAge(), infoUserInput.getActivityLevel(), infoUserInput.getGoal());
        infoUserRepository.save(infoUser);

      

        return InfoUserResponseDto.fromEntity(infoUser);
    }

    private DietInput getMacros(InfoUserInput infoUserInput) {
        DietInput dietInput = null;
        Integer genderId = userRepository.findGenderIdById(infoUserInput.getUserId());

        switch (infoUserInput.getGoal()) {
            case 1:

                break;
            case 2:

                break;
            case 3:

                break;
            default:
                throw new IllegalArgumentException("Invalid goal: " + infoUserInput.getGoal());
        }

        return dietInput;
    }

    private DietInput macrosCalculator(Integer genderId, InfoUserInput infoUserInput) {
        Integer BMR = 0;

        if (genderId == 1) {
            BMR = (int) (10 * infoUserInput.getWheight() + 6.25 * infoUserInput.getHeight() - 5 * infoUserInput.getAge() + 5);
        } else if (genderId == 2) {
            BMR = (int) (10 * infoUserInput.getWheight() + 6.25 * infoUserInput.getHeight() - 5 * infoUserInput.getAge() - 161);
        } else {
            throw new IllegalArgumentException("Invalid gender: " + genderId);
        }



        return DietInput.builder()
                .calories(BMR)
                .carbohydrates(250)
                .proteins(150)
                .fats(70)
                .build();
    }

}