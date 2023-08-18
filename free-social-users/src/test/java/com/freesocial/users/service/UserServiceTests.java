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

@SpringBootTest
class UserServiceTests extends BasicTest {

    @MockBean
    private UserAuthenticationService userAuthenticationService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private UserUtils userUtils;

    private UserSignUpDTO userDto;

    @BeforeEach
    void prepareDto() {
        userDto = new UserSignUpDTO();
        userDto.setName("New user");
        userDto.setUsername(userDto.getName());
        userDto.setPassword("123123");
        userDto.setPasswordConfirm(userDto.getPassword());
    }

    @AfterEach
    void deleteAll() {
        userRepository.deleteAll();
    }

    @Test
    void createUser() {
        //Will ignore validation
        doNothing().when(userAuthenticationService).validateNewUser(Mockito.any(UserAuthentication.class));

        assertEquals(0, userRepository.count(), "Should be 0");

        FreeSocialUser testUser = userService.create(FreeSocialUser.of(userDto));

        assertEquals(1, userRepository.count(), "Should be 1");

        assertNotNull(testUser, "User must exists");

        assertNotNull(testUser.getId(), "ID must exists");

        assertNotNull(testUser.getUuid(), "UUID must exists");

        assertEquals(userDto.getName(), testUser.getProfile().getName(),
                "Name must be equals to the DTO name");

        assertEquals(userUtils.prepareUsername(userDto.getUsername()),
                testUser.getAuthentication().getUsername(),
                "Username must be equals to prepared DTO username");

        Optional<FreeSocialUser> userOptional = userRepository.findByUuid(testUser.getUuid());
        FreeSocialUser userFromDb = userOptional.get();

        assertTrue(userOptional.isPresent(), "User must exists in DB");

        assertEquals(testUser.getId(), userFromDb.getId(), "Must be the same ID");

        assertEquals(testUser.getUuid(), userFromDb.getUuid(), "Must be the same UUID");
    }

    @Test
    void deleteUser() {
        assertEquals(0, userRepository.count(), "Should be 0");

        FreeSocialUser testUser = userService.create(FreeSocialUser.of(userDto));

        assertEquals(1, userRepository.count(), "Should be 1");

        userService.delete(testUser);

        assertEquals(0, userRepository.count(), "Should be 0 again");

    }

}
