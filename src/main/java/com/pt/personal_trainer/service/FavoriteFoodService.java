package com.pt.personal_trainer.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.pt.personal_trainer.domain.dto.FavoriteFoodDto;
import com.pt.personal_trainer.domain.input.FavoriteFoodInput;
import com.pt.personal_trainer.entity.FavoriteFood;
import com.pt.personal_trainer.exception.CustomExceptions.NotFoundException;
import com.pt.personal_trainer.exception.CustomExceptions.ProcessServiceException;
import com.pt.personal_trainer.exception.CustomExceptions.ServerErrorException;
import com.pt.personal_trainer.repository.FavoriteFoodRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class FavoriteFoodService {

    private final FavoriteFoodRepository favoriteFoodRepository;

    @Transactional
    public FavoriteFoodDto addFavorite(Long userId, FavoriteFoodInput input) {
        if (favoriteFoodRepository.existsByUserIdAndFdcId(userId, input.getFdcId())) {
            throw new ProcessServiceException("Food is already in your favorites.");
        }

        FavoriteFood entity = new FavoriteFood();
        entity.setUserId(userId);
        entity.setFdcId(input.getFdcId());
        entity.setName(input.getName());
        entity.setBrand(input.getBrand());
        entity.setDataType(input.getDataType());
        entity.setCalories(input.getCalories());
        entity.setProteinG(input.getProteinG());
        entity.setCarbsG(input.getCarbsG());
        entity.setFatG(input.getFatG());
        entity.setFiberG(input.getFiberG());

        if (input.getVitamins() != null) {
            entity.setVitaminAUg(input.getVitamins().getVitaminAUg());
            entity.setVitaminCMg(input.getVitamins().getVitaminCMg());
            entity.setVitaminDUg(input.getVitamins().getVitaminDUg());
            entity.setVitaminEMg(input.getVitamins().getVitaminEMg());
            entity.setVitaminKUg(input.getVitamins().getVitaminKUg());
            entity.setVitaminB6Mg(input.getVitamins().getVitaminB6Mg());
            entity.setVitaminB12Ug(input.getVitamins().getVitaminB12Ug());
        }

        try {
            return FavoriteFoodDto.fromEntity(favoriteFoodRepository.save(entity));
        } catch (Exception e) {
            log.error("Failed to save favorite food for userId={}", userId, e);
            throw new ServerErrorException("Failed to save favorite food.");
        }
    }

    @Transactional
    public void removeFavorite(Long userId, Integer fdcId) {
        if (!favoriteFoodRepository.existsByUserIdAndFdcId(userId, fdcId)) {
            throw new NotFoundException("Favorite food not found.");
        }
        try {
            favoriteFoodRepository.deleteByUserIdAndFdcId(userId, fdcId);
        } catch (Exception e) {
            log.error("Failed to remove favorite food fdcId={} for userId={}", fdcId, userId, e);
            throw new ServerErrorException("Failed to remove favorite food.");
        }
    }

    public List<FavoriteFoodDto> getFavorites(Long userId) {
        try {
            return favoriteFoodRepository.findByUserIdOrderByCreatedAtDesc(userId)
                    .stream()
                    .map(FavoriteFoodDto::fromEntity)
                    .toList();
        } catch (Exception e) {
            log.error("Failed to retrieve favorites for userId={}", userId, e);
            throw new ServerErrorException("Failed to retrieve favorite foods.");
        }
    }
}
