package com.freesocial.security.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.freesocial.lib.config.tests.BasicTest;
import com.freesocial.security.FreeSocialSecurityApplication;
import com.freesocial.security.dto.AuthRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.notNullValue;


@Sql(value = "/authdata.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = "/cleardata.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@SpringBootTest(classes = FreeSocialSecurityApplication.class)
@AutoConfigureWebTestClient
class AuthenticationControllerIntegrationTests extends BasicTest {

    @Autowired
    private WebTestClient webTestClient;

    @Test
    void authenticateWithWrongArguments() throws Exception {
        webTestClient.post().uri("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new ObjectMapper().writeValueAsString(new AuthRequest()))
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.*", hasSize(2));

    }

    @Test
    void authenticateWithIncorrectInformation() throws Exception {
        AuthRequest authRequest = new AuthRequest();
        authRequest.setUsername("abc");
        authRequest.setPassword("123");

        webTestClient.post().uri("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new ObjectMapper().writeValueAsString(authRequest))
                .exchange()
                .expectStatus().isUnauthorized();
    }

    @Test
    void authenticateWithDisabledUser() throws Exception {
        AuthRequest authRequest = new AuthRequest();
        authRequest.setUsername("tester1");
        authRequest.setPassword("123123");

        webTestClient.post().uri("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new ObjectMapper().writeValueAsString(authRequest))
                .exchange()
                .expectStatus().isUnauthorized();
    }

    @Test
    void authenticateWithCorrectUser() throws Exception {
        AuthRequest authRequest = new AuthRequest();
        authRequest.setUsername("joseph");
        authRequest.setPassword("123123");

        webTestClient.post().uri("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new ObjectMapper().writeValueAsString(authRequest))
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.token", notNullValue());
    }

}
