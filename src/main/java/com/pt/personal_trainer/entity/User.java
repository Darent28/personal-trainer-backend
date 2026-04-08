package com.pt.personal_trainer.entity;

import java.io.Serializable;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
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

    @Column(name = "status")
    private Integer status;

    @Column(name = "gender_id")
    private Integer genderId;

    @Column(name = "email_verified")
    private Boolean emailVerified = false;

    public User(String username, String email, String password, Integer genderId) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.genderId = genderId;
        this.status = 1;
        this.emailVerified = false;
    }
}
