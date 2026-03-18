package com.pt.personal_trainer.entity;

import java.io.Serializable;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Data
@Table(name = "users")
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "username")
    private String username;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @ManyToOne()
    @JoinColumn(name = "gender_id", referencedColumnName = "id")
    private Integer gender_id;

    public User(String username, String email, String password, Integer gender_id) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.gender_id = gender_id;
    }
}
