package com.freesocial.gateway.controller.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.freesocial.gateway.config.security.SpringCloudConfig;
import com.freesocial.gateway.controller.AuthenticationController;
import com.freesocial.gateway.dto.AuthRequest;
import com.freesocial.gateway.repository.UserAuthenticationRepository;
import com.freesocial.gateway.repository.UserRepository;
import com.freesocial.gateway.service.UserAuthenticationService;
import com.freesocial.lib.config.security.services.JwtAuthenticationFilter;
import com.freesocial.lib.config.tests.BasicTest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.reactive.ReactiveSecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cloud.gateway.filter.factory.SaveSessionGatewayFilterFactory;
import org.springframework.cloud.gateway.filter.factory.TokenRelayGatewayFilterFactory;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@Sql(value = "/authdata.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = "/cleardata.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@SpringBootTest
@AutoConfigureWebTestClient
class AuthenticationControllerIntegrationTests extends BasicTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserAuthenticationService userAuthenticationService;

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
