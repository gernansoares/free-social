package com.freesocial.users.controller.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.freesocial.lib.config.tests.BasicTest;
import com.freesocial.users.common.util.UserUtils;
import com.freesocial.users.dto.UserSignUpDTO;
import com.freesocial.users.repository.UserAuthenticationRepository;
import com.freesocial.users.repository.UserRepository;
import com.freesocial.users.service.UserAuthenticationService;
import com.freesocial.users.service.UserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import static org.hamcrest.Matchers.aMapWithSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
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

    @AfterEach
    void deleteAll() {
        userRepository.deleteAll();
    }

    @Test
    void createUser() throws Exception {
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

        assertNotNull(userAuthenticationRepository.getByUsernameIgnoreCase(userToBeAdd.getUsername()),
                "User must exists");
    }

}
