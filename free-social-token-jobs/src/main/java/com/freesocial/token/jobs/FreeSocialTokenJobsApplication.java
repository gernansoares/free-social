package com.freesocial.token.jobs;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("com.freesocial")
public class FreeSocialTokenJobsApplication {

    public static void main(String[] args) {
        SpringApplication.run(FreeSocialTokenJobsApplication.class, args);
    }

}