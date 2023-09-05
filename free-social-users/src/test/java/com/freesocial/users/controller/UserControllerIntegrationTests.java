package com.freesocial.users.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.freesocial.lib.config.security.JwtAuthenticationFilter;
import com.freesocial.lib.config.tests.BasicTest;
import com.freesocial.lib.common.util.Profiles;
import com.freesocial.users.FreeSocialUsersApplication;
import com.freesocial.users.common.util.UserUtils;
import com.freesocial.users.dto.UserAuthenticationDTO;
import com.freesocial.users.dto.UserProfileDTO;
import com.freesocial.users.dto.UserSignUpDTO;
import com.freesocial.users.entity.FreeSocialUser;
import com.freesocial.users.repository.UserAuthenticationRepository;
import com.freesocial.users.repository.UserProfileRepository;
import com.freesocial.users.repository.UserRepository;
import com.freesocial.users.service.UserService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@SpringBootTest(classes = FreeSocialUsersApplication.class)
@AutoConfigureWebTestClient
@ActiveProfiles(profiles = Profiles.TESTS_NO_SECURITY)
class UserControllerIntegrationTests extends BasicTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserAuthenticationRepository userAuthenticationRepository;

    @Autowired
    private UserProfileRepository userProfileRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private WebTestClient webTestClient;

    private FreeSocialUser addedUser;

    private UserSignUpDTO userToBeAdd;

    @BeforeAll
    static void setup() {
        new UserUtils().setEncoder(new BCryptPasswordEncoder());
    }

    @BeforeEach
    public void deleteAllAndCreateOneUser() {
        userRepository.deleteAll();
        userAuthenticationRepository.deleteAll();
        userProfileRepository.deleteAll();

        userToBeAdd = new UserSignUpDTO();
        userToBeAdd.setName("Johnny User");
        userToBeAdd.setBio("Bio");
        userToBeAdd.setUsername(userToBeAdd.getName());
        userToBeAdd.setPassword("123456");
        userToBeAdd.setPasswordConfirm(userToBeAdd.getPassword());

        addedUser = userService.create(FreeSocialUser.of(userToBeAdd));
    }

    @Test
    void updateUserProfile() throws Exception {
        UserProfileDTO userProfileDTO = new UserProfileDTO();
        userProfileDTO.setName("New name");
        userProfileDTO.setBio("New bio");

        webTestClient.put().uri("/users/profile")
                .header(JwtAuthenticationFilter.HEADER_UUID, addedUser.getUuid())
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new ObjectMapper().writeValueAsString(userProfileDTO))
                .exchange()
                .expectStatus().isOk();

        //Updates the user
        addedUser = userRepository.findByUuid(addedUser.getUuid()).get();

        assertNotEquals(userToBeAdd.getName(), addedUser.getProfile().getName(), "Name must not be equals");

        assertNotEquals(userToBeAdd.getBio(), addedUser.getProfile().getBio(), "Bio must not be equals");

        assertEquals(addedUser.getProfile().getName(), userProfileDTO.getName(), "Name must be equals");

        assertEquals(addedUser.getProfile().getBio(), userProfileDTO.getBio(), "Bio must be equals");
    }

    @Test
    void updateUserAuthentication() throws Exception {
        UserAuthenticationDTO userPasswordDto = new UserAuthenticationDTO();
        userPasswordDto.setUsername("New User");
        userPasswordDto.setPassword("654321");
        userPasswordDto.setPasswordConfirm("654321");

        webTestClient.put().uri("/users/authentication")
                .header(JwtAuthenticationFilter.HEADER_UUID, addedUser.getUuid())
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new ObjectMapper().writeValueAsString(userPasswordDto))
                .exchange()
                .expectStatus().isOk();

        //Updates the user
        addedUser = userRepository.findByUuid(addedUser.getUuid()).get();

        assertNotEquals(UserUtils.prepareUsername(userToBeAdd.getUsername()), addedUser.getAuthentication().getUsername(),
                "Username must not be equals");

        assertFalse(BCrypt.checkpw(userToBeAdd.getPassword(), addedUser.getAuthentication().getPassword()),
                "Password must not be equals");

        assertEquals(UserUtils.prepareUsername(userPasswordDto.getUsername()), addedUser.getAuthentication().getUsername(),
                "Username must be equals");

        assertTrue(BCrypt.checkpw(userPasswordDto.getPassword(), addedUser.getAuthentication().getPassword()),
                "Password must be equals");
    }

    @Test
    void deleteUser() throws Exception {
        webTestClient.delete().uri("/users")
                .header(JwtAuthenticationFilter.HEADER_UUID, addedUser.getUuid())
                .exchange()
                .expectStatus().isOk();

        assertEquals(0, userRepository.count(), "Must be 0");
    }

}
