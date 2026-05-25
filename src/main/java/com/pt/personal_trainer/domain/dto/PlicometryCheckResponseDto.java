package com.pt.personal_trainer.domain.dto;

import com.pt.personal_trainer.entity.PlicometryCheck;

public record PlicometryCheckResponseDto(
    Long id,
    Long userId,
    Integer method,
    Double fatPercentage,
    Double siteChest,
    Double siteMidaxillary,
    Double siteTriceps,
    Double siteSubscapular,
    Double siteAbdomen,
    Double siteSuprailiac,
    Double siteThigh,
    Double siteIliacCrest,
    String createdAt
) {

    public static PlicometryCheckResponseDto fromEntity(PlicometryCheck e) {
        return new PlicometryCheckResponseDto(
            e.getId(),
            e.getUserId(),
            e.getMethod(),
            e.getFatPercentage(),
            e.getSiteChest(),
            e.getSiteMidaxillary(),
            e.getSiteTriceps(),
            e.getSiteSubscapular(),
            e.getSiteAbdomen(),
            e.getSiteSuprailiac(),
            e.getSiteThigh(),
            e.getSiteIliacCrest(),
            e.getCreatedAt() != null ? e.getCreatedAt().toString() : null
        );
    }
}
