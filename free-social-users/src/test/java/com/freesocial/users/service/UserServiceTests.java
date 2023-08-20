package com.freesocial.users.service;

import com.freesocial.lib.config.tests.BasicTest;
import com.freesocial.users.common.util.UserUtils;
import com.freesocial.users.dto.UserSignUpDTO;
import com.freesocial.users.entity.FreeSocialUser;
import com.freesocial.users.entity.UserAuthentication;
import com.freesocial.users.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@SpringBootTest
class UserServiceTests extends BasicTest {

    @MockBean
    private UserAuthenticationService userAuthenticationService;

    @MockBean
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private UserUtils userUtils;

    @Test
    void createUser() {
        UserSignUpDTO userDto = new UserSignUpDTO();
        userDto.setName("New user");
        userDto.setUsername(userDto.getName());
        userDto.setPassword("123123");
        userDto.setPasswordConfirm(userDto.getPassword());

        FreeSocialUser usedToBeAdd = FreeSocialUser.of(userDto);

        //Will return the same object on save
        when(userRepository.save(Mockito.any(FreeSocialUser.class))).thenReturn(usedToBeAdd);

        //Will return empty on search
        when(userRepository.findByUuid(usedToBeAdd.getUuid())).thenReturn(Optional.empty());

        //Will ignore validation
        doNothing().when(userAuthenticationService).validateNewUser(Mockito.any(UserAuthentication.class));

        FreeSocialUser testUser = userService.create(usedToBeAdd);

        assertNotNull(testUser, "User must exists");

        assertNull(testUser.getId(), "ID must not exists (database generated)");

        assertNotNull(testUser.getUuid(), "UUID must exists");

        assertEquals(userDto.getName(), testUser.getProfile().getName(),
                "Name must be equals to the DTO name");

        assertEquals(userUtils.prepareUsername(userDto.getUsername()),
                testUser.getAuthentication().getUsername(),
                "Username must be equals to prepared DTO username");

        Optional<FreeSocialUser> userOptional = userRepository.findByUuid(testUser.getUuid());

        assertFalse(userOptional.isPresent(), "User not must exists");

    }

}
