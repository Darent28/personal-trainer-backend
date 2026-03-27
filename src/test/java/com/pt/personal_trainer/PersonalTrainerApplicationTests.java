package com.pt.personal_trainer;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import com.pt.personal_trainer.controller.UserController;

@SpringBootTest
@ActiveProfiles("Test")
@TestPropertySource(locations = "classpath:application-test.properties")
@EnableAutoConfiguration
class PersonalTrainerApplicationTests {

	@Autowired
	UserController userController;

	@Test
	void contextLoads() { Assertions.assertThat(userController).isNotNull();}

}
