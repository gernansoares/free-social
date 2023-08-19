package com.freesocial.users.controller.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.freesocial.lib.config.tests.BasicTest;
import com.freesocial.users.common.util.UserUtils;
import com.freesocial.users.dto.UserSignUpDTO;
import com.freesocial.users.entity.FreeSocialUser;
import com.freesocial.users.entity.UserAuthentication;
import com.freesocial.users.repository.UserAuthenticationRepository;
import com.freesocial.users.repository.UserRepository;
import com.freesocial.users.service.UserAuthenticationService;
import com.freesocial.users.service.UserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@SpringBootTest
class NewUserControllerIntegrationTests extends BasicTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserAuthenticationRepository userAuthenticationRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserUtils userUtils;

    @BeforeEach
    void deleteAll() {
        userRepository.deleteAll();
    }

    @Test
    void createUserWithIncorrectArguments() throws Exception {
        UserSignUpDTO userToBeAdd = new UserSignUpDTO();
        userToBeAdd.setUsername("Tester");

        assertEquals(0, userRepository.count(), "Must be 0");

        mockMvc.perform(post("/newuser")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(userToBeAdd)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.*", hasSize(3)))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        assertEquals(0, userRepository.count(), "Must be 0");

        assertFalse(userAuthenticationRepository
                        .getByUsernameIgnoreCase(userUtils.prepareUsername(userToBeAdd.getUsername()))
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

        assertEquals(0, userRepository.count(), "Must be 0");

        mockMvc.perform(post("/newuser")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(userToBeAdd)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", is(userToBeAdd.getName())))
                .andExpect(jsonPath("$.username", is(userUtils.prepareUsername(userToBeAdd.getUsername()))))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        assertEquals(1, userRepository.count(), "Must be 1");

        assertNotNull(userAuthenticationRepository.getByUsernameIgnoreCase(
                userUtils.prepareUsername(userToBeAdd.getUsername())),"User must exists");
    }

}
