package com.pt.personal_trainer.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.pt.personal_trainer.domain.dto.PlicometryCheckResponseDto;
import com.pt.personal_trainer.domain.input.PlicometryCheckInput;
import com.pt.personal_trainer.entity.PlicometryCheck;
import com.pt.personal_trainer.exception.CustomExceptions.NotFoundException;
import com.pt.personal_trainer.exception.CustomExceptions.ServerErrorException;
import com.pt.personal_trainer.repository.PlicometryCheckRepository;
import com.pt.personal_trainer.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class PlicometryCheckService {

    private final PlicometryCheckRepository plicometryCheckRepository;
    private final UserRepository userRepository;

    public PlicometryCheckResponseDto save(PlicometryCheckInput input) {
        userRepository.findById(input.getUserId())
            .orElseThrow(() -> new NotFoundException("User not found with id: " + input.getUserId()));

        try {
            PlicometryCheck check = new PlicometryCheck();
            check.setUserId(input.getUserId());
            check.setMethod(input.getMethod());
            check.setFatPercentage(input.getFatPercentage());
            check.setSiteChest(input.getSiteChest());
            check.setSiteMidaxillary(input.getSiteMidaxillary());
            check.setSiteTriceps(input.getSiteTriceps());
            check.setSiteSubscapular(input.getSiteSubscapular());
            check.setSiteAbdomen(input.getSiteAbdomen());
            check.setSiteSuprailiac(input.getSiteSuprailiac());
            check.setSiteThigh(input.getSiteThigh());
            check.setSiteIliacCrest(input.getSiteIliacCrest());

            plicometryCheckRepository.save(check);
            return PlicometryCheckResponseDto.fromEntity(check);
        } catch (NotFoundException e) {
            throw e;
        } catch (Exception e) {
            log.error("Failed to save plicometry check for userId={}", input.getUserId(), e);
            throw new ServerErrorException("Failed to save plicometry check. Please try again later.");
        }
    }

    public List<PlicometryCheckResponseDto> getByUserId(Long userId) {
        try {
            return plicometryCheckRepository.findByUserIdOrderByCreatedAtDesc(userId).stream()
                .map(PlicometryCheckResponseDto::fromEntity)
                .toList();
        } catch (Exception e) {
            log.error("Failed to retrieve plicometry history for userId={}", userId, e);
            throw new ServerErrorException("Failed to retrieve plicometry history. Please try again later.");
        }
    }
}
