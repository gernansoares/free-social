package com.freesocial.users.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.freesocial.lib.config.security.JwtAuthenticationFilter;
import com.freesocial.lib.config.tests.BasicTest;
import com.freesocial.users.FreeSocialUsersApplication;
import com.freesocial.users.common.util.UserUtils;
import com.freesocial.users.dto.UserAuthenticationDTO;
import com.freesocial.users.dto.UserProfileDTO;
import com.freesocial.users.dto.UserSignUpDTO;
import com.freesocial.users.entity.FreeSocialUser;
import com.freesocial.users.repository.UserAuthenticationRepository;
import com.freesocial.users.repository.UserRepository;
import com.freesocial.users.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@SpringBootTest(classes = FreeSocialUsersApplication.class)
@AutoConfigureWebTestClient
class NewUserControllerIntegrationTests extends BasicTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserAuthenticationRepository userAuthenticationRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private UserUtils userUtils;

    @BeforeEach
    void deleteAll() {
        userRepository.deleteAll();
    }

    @Test
    void createUserWithIncorrectArguments() throws Exception {
        UserSignUpDTO userToBeAdd = new UserSignUpDTO();
        userToBeAdd.setUsername("new tester");
        userToBeAdd.setName("");
        userToBeAdd.setPassword("");
        userToBeAdd.setPasswordConfirm(userToBeAdd.getPassword());

        webTestClient.post().uri("/newuser")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new ObjectMapper().writeValueAsString(userToBeAdd))
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.*", hasSize(3));

        assertEquals(0, userRepository.count(), "Must be 0");

        assertFalse(userAuthenticationRepository
                        .findByUsernameIgnoreCase(userUtils.prepareUsername(userToBeAdd.getUsername()))
                        .isPresent(),
                "User must not exists");
    }

    @Test
    void createUserWithCorrectArguments() throws Exception {
        UserSignUpDTO userToBeAdd = new UserSignUpDTO();
        userToBeAdd.setName("Johnny User");
        userToBeAdd.setUsername(userToBeAdd.getName());
        userToBeAdd.setPassword("123456");
        userToBeAdd.setPasswordConfirm(userToBeAdd.getPassword());

        webTestClient.post().uri("/newuser")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new ObjectMapper().writeValueAsString(userToBeAdd))
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.name").isEqualTo(userToBeAdd.getName())
                .jsonPath("$.username").isEqualTo(userUtils.prepareUsername(userToBeAdd.getUsername()));

        assertEquals(1, userRepository.count(), "Must be 1");

        assertNotNull(userAuthenticationRepository.findByUsernameIgnoreCase(
                userUtils.prepareUsername(userToBeAdd.getUsername())), "User must exists");
    }

}
