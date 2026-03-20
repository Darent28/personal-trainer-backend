package com.pt.personal_trainer.service;

import org.springframework.stereotype.Service;
import com.pt.personal_trainer.domain.dto.InfoUserResponseDto;
import com.pt.personal_trainer.domain.input.InfoUserInput;
import com.pt.personal_trainer.entity.GoalType;
import com.pt.personal_trainer.entity.InfoUser;
import com.pt.personal_trainer.exception.CustomExceptions.NotFoundException;
import com.pt.personal_trainer.repository.InfoUserRepository;
import com.pt.personal_trainer.repository.UserRepository;
import com.pt.personal_trainer.repository.GoalTypeRepository;

import jakarta.transaction.Transactional;

@Service
public class InfoUserService {

    private final UserRepository userRepository;
    private final InfoUserRepository infoUserRepository;
    private final GoalTypeRepository goalTypeRepository;

    public InfoUserService(UserRepository userRepository, InfoUserRepository infoUserRepository, GoalTypeRepository goalTypeRepository) {
        this.userRepository = userRepository;
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

//     private DietInput getMacros(InfoUserInput infoUserInput) {
//         DietInput dietInput = null;
//         Integer genderId = userRepository.findGenderIdById(infoUserInput.getUserId());

//         switch (infoUserInput.getGoal()) {
//             case 1:

//                 break;
//             case 2:

//                 break;
//             case 3:

//                 break;
//             default:
//                 throw new IllegalArgumentException("Invalid goal: " + infoUserInput.getGoal());
//         }

//         return dietInput;
//     }

//     private DietInput macrosCalculator(Integer genderId, InfoUserInput infoUserInput) {
//         Integer BMR = 0;

//         if (genderId == 1) {
//             BMR = (int) (10 * infoUserInput.getWheight() + 6.25 * infoUserInput.getHeight() - 5 * infoUserInput.getAge() + 5);
//         } else if (genderId == 2) {
//             BMR = (int) (10 * infoUserInput.getWheight() + 6.25 * infoUserInput.getHeight() - 5 * infoUserInput.getAge() - 161);
//         } else {
//             throw new IllegalArgumentException("Invalid gender: " + genderId);
//         }



//         return DietInput.builder()
//                 .calories(BMR)
//                 .carbohydrates(250)
//                 .proteins(150)
//                 .fats(70)
//                 .build();
//     }

}