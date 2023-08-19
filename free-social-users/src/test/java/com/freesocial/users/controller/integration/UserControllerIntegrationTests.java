package com.freesocial.users.controller.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.freesocial.lib.config.security.services.JwtAuthenticationFilter;
import com.freesocial.lib.config.tests.BasicTest;
import com.freesocial.users.dto.UserProfileDTO;
import com.freesocial.users.dto.UserSignUpDTO;
import com.freesocial.users.entity.FreeSocialUser;
import com.freesocial.users.repository.UserRepository;
import com.freesocial.users.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc(addFilters = false)
@SpringBootTest
class UserControllerIntegrationTests extends BasicTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private MockMvc mockMvc;

    private FreeSocialUser addedUser;

    private UserSignUpDTO userToBeAdd;

    @BeforeEach
    public void deleteAllAndCreateOneUser() {
        userRepository.deleteAll();

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
        assertEquals(1, userRepository.count(), "Must be 1");

        assertEquals(userToBeAdd.getName(), addedUser.getProfile().getName(), "Name must be equals");

        assertEquals(userToBeAdd.getBio(), addedUser.getProfile().getBio(), "Bio must be equals");

        UserProfileDTO userProfileDTO = new UserProfileDTO();
        userProfileDTO.setName("New name");
        userProfileDTO.setBio("New bio");

        mockMvc.perform(put("/users")
                .header(JwtAuthenticationFilter.HEADER_UUID, addedUser.getUuid())
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(userProfileDTO)))
                .andExpect(status().isOk());

        //Updates the user
        addedUser = userRepository.findByUuid(addedUser.getUuid()).get();

        assertNotEquals(userToBeAdd.getName(), addedUser.getProfile().getName(), "Name must not be equals");

        assertNotEquals(userToBeAdd.getBio(), addedUser.getProfile().getBio(), "Bio must not be equals");

        assertEquals(addedUser.getProfile().getName(), userProfileDTO.getName(), "Name must be equals");

        assertEquals(addedUser.getProfile().getBio(), userProfileDTO.getBio(), "Bio must be equals");
    }

    @Test
    void deleteUser() throws Exception {
        assertEquals(1, userRepository.count(), "Must be 1");

        mockMvc.perform(delete("/users")
                .header(JwtAuthenticationFilter.HEADER_UUID, addedUser.getUuid()))
                .andExpect(status().isOk());

        assertEquals(0, userRepository.count(), "Must be 0");
    }

}
