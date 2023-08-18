package com.freesocial.users.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.freesocial.lib.config.tests.BasicTest;
import com.freesocial.users.common.util.UserUtils;
import com.freesocial.users.controller.NewUserController;
import com.freesocial.users.dto.UserSignUpDTO;
import com.freesocial.users.entity.FreeSocialUser;
import com.freesocial.users.service.UserAuthenticationService;
import com.freesocial.users.service.UserProfileService;
import com.freesocial.users.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.aMapWithSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(NewUserController.class)
class NewUserControllerTests extends BasicTest {

    @MockBean
    private UserService userService;

    @MockBean
    private UserAuthenticationService userAuthenticationService;

    @MockBean
    private UserProfileService userProfileService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserUtils userUtils;

    @Test
    void createUserWithInvalidArguments() throws Exception {
        UserSignUpDTO userToBeAdd = new UserSignUpDTO();
        userToBeAdd.setName(null);
        userToBeAdd.setUsername("N");
        userToBeAdd.setPassword("123");
        userToBeAdd.setPasswordConfirm("1234");

        mockMvc.perform(post("/newuser")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(userToBeAdd)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$", aMapWithSize(4)))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void createUserWithCorrectArguments() throws Exception {
        UserSignUpDTO userToBeAdd = new UserSignUpDTO();
        userToBeAdd.setName("Johnny User");
        userToBeAdd.setUsername(userToBeAdd.getName());
        userToBeAdd.setPassword("123456");
        userToBeAdd.setPasswordConfirm(userToBeAdd.getPassword());

        FreeSocialUser user = FreeSocialUser.of(userToBeAdd);

        //User will be created
        when(userService.create(user)).thenReturn(user);

        mockMvc.perform(post("/newuser")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(userToBeAdd)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", is(userToBeAdd.getName())))
                .andExpect(jsonPath("$.username", is(userUtils.prepareUsername(userToBeAdd.getUsername()))))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

}
