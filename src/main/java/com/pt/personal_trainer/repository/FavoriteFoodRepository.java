package com.pt.personal_trainer.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pt.personal_trainer.entity.FavoriteFood;

public interface FavoriteFoodRepository extends JpaRepository<FavoriteFood, Long> {

    List<FavoriteFood> findByUserIdOrderByCreatedAtDesc(Long userId);

    boolean existsByUserIdAndFdcId(Long userId, Integer fdcId);

    void deleteByUserIdAndFdcId(Long userId, Integer fdcId);
}
