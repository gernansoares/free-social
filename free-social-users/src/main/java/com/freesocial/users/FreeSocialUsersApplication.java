package com.freesocial.users;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("com.freesocial")
public class FreeSocialUsersApplication {

	public static void main(String[] args) {
		SpringApplication.run(FreeSocialUsersApplication.class, args);
	}

}
