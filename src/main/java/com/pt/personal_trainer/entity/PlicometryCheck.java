package com.pt.personal_trainer.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

@Entity
@Table(name = "plicometry_check")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PlicometryCheck implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "method", nullable = false)
    private Integer method;

    @Column(name = "fat_percentage", nullable = false)
    private Double fatPercentage;

    @Column(name = "site_chest")
    private Double siteChest;

    @Column(name = "site_midaxillary")
    private Double siteMidaxillary;

    @Column(name = "site_triceps")
    private Double siteTriceps;

    @Column(name = "site_subscapular")
    private Double siteSubscapular;

    @Column(name = "site_abdomen")
    private Double siteAbdomen;

    @Column(name = "site_suprailiac")
    private Double siteSuprailiac;

    @Column(name = "site_thigh")
    private Double siteThigh;

    @Column(name = "site_iliac_crest")
    private Double siteIliacCrest;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
}
