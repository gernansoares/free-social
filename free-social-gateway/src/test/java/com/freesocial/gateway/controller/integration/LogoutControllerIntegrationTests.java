package com.freesocial.gateway.controller.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.freesocial.gateway.dto.AuthRequest;
import com.freesocial.gateway.repository.UserRepository;
import com.freesocial.gateway.service.UserAuthenticationService;
import com.freesocial.lib.config.security.JwtUtil;
import com.freesocial.lib.config.security.services.JwtAuthenticationFilter;
import com.freesocial.lib.config.tests.BasicTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.notNullValue;


@SpringBootTest
@AutoConfigureWebTestClient
class LogoutControllerIntegrationTests extends BasicTest {

    @Autowired
    private WebTestClient webTestClient;

    @Test
    void logoutWithoutToken()  {
        webTestClient.post().uri("/logout/logout")
                .exchange()
                .expectStatus().isUnauthorized();
    }

    @Test
    void logoutWithIncorrectToken() {
        webTestClient.post().uri("/logout/logout")
                .headers(http -> http.setBearerAuth(""))
                .exchange()
                .expectStatus().isUnauthorized();
    }

    @Sql(value = "/tokendata.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = "/cleartoken.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @Test
    void logoutWithCorrectToken() {
        String token = "eyJhbGciOiJIUzUxMiJ9.eyJyb2xlIjpbIlJPTEVfVVNFUiIsIlJPTEVfUE9TVCIsIlJPTEVfRkVFRCJdLCJzdWIiOiJ0ZXN0ZXIxIiwiYXVkIjoiMjQzMjM0ODk3MjMiLCJpYXQiOjE2OTI2MjQxOTUsImV4cCI6MTY5MzEyNDE5NX0.jLQ15DRL-KUeb9Nl88iezPXS5omE3mtMeDK2w4qbYdZLHDXc3w7rMc0z4FlR-bEr431C22TpIIhJ0OSqKSZOeA";

        webTestClient.post().uri("/logout/logout")
                .headers(http -> http.setBearerAuth(token))
                .exchange()
                .expectStatus().isOk();
    }

}
