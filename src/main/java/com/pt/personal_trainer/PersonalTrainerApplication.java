package com.pt.personal_trainer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class PersonalTrainerApplication {

	public static void main(String[] args) {
		SpringApplication.run(PersonalTrainerApplication.class, args);
	}

}
