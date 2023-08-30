package com.freesocial.security.integration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.freesocial.lib.config.tests.BasicTest;
import com.freesocial.security.FreeSocialSecurityApplication;
import com.freesocial.security.dto.AuthRequest;
import com.freesocial.security.dto.AuthResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.hamcrest.Matchers.notNullValue;

@SpringBootTest(classes = FreeSocialSecurityApplication.class)
@AutoConfigureWebTestClient
class LogoutControllerIntegrationTests extends BasicTest {

    @Autowired
    private WebTestClient webTestClient;

    @Test
    void logoutWithoutToken() {
        webTestClient.post().uri("/logout/logout")
                .exchange()
                .expectStatus().isUnauthorized();
    }

    @Test
    void logoutWithIncorrectToken() {
        webTestClient.post().uri("/logout/logout")
                .headers(http -> http.setBearerAuth("eyJhbGciOiJIUzUxMiJ9.eyJyb2xlIjpbIlJPTEVfVVNFUiIsIlJPTEVfUE9TVCIsIlJPTEVfRkVFRCJdLCJzdWIiOiJ0ZXN0ZXIxIiwiYXVkIjoiMjQzMjM0ODk3MjMiLCJpYXQiOjE2OTI2MjQxOTUsImV4cCI6MTY5MzEyNDE5NX0.jLQ15DRL-KUeb9Nl88iezPXS5omE3mtMeDK2w4qbYdZLHDXc3w7rMc0z4FlR-bEr431C22TpIIhJ0OSqKSZOeA"))
                .exchange()
                .expectStatus().isUnauthorized();
    }

    @Sql(value = "/authdata.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = "/cleardata.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @Test
    void logoutWithCorrectToken() throws JsonProcessingException {
        AuthRequest authRequest = new AuthRequest();
        authRequest.setUsername("joseph");
        authRequest.setPassword("123123");

        EntityExchangeResult<AuthResponse> token = webTestClient.post().uri("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new ObjectMapper().writeValueAsString(authRequest))
                .exchange()
                .expectStatus().isOk()
                .expectBody(AuthResponse.class)
                .returnResult();

        webTestClient.post().uri("/logout/logout")
                .headers(http -> http.setBearerAuth(token.getResponseBody().getToken()))
                .exchange()
                .expectStatus().isOk();
    }

}