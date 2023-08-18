package com.freesocial.users.service;

import com.freesocial.lib.config.tests.BasicTest;
import com.freesocial.users.dto.UserSignUpDTO;
import com.freesocial.users.entity.FreeSocialUser;
import com.freesocial.users.repository.UserAuthenticationRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserAuthenticationServiceTests extends BasicTest {

    @Mock
    private UserAuthenticationRepository userAuthenticationRepository;

    @InjectMocks
    private UserAuthenticationService userAuthenticationService;

    @Test
    void validateNewUserWithIncorrectPasswords() {
        UserSignUpDTO userToBeAdd = new UserSignUpDTO();
        userToBeAdd.setUsername("");
        userToBeAdd.setPassword("123456");
        userToBeAdd.setPasswordConfirm("");

        FreeSocialUser user = FreeSocialUser.of(userToBeAdd);

        //Username will not exists
        when(userAuthenticationRepository.getByUsernameIgnoreCase(user.getAuthentication().getUsername()))
                .thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> {
            userAuthenticationService.validateNewUser(user.getAuthentication());
        }, "User password and confirmation are not the same");

        userToBeAdd.setPasswordConfirm(userToBeAdd.getPassword());

        assertDoesNotThrow(() -> {
            userAuthenticationService.validateNewUser(FreeSocialUser.of(userToBeAdd).getAuthentication());
        }, "Everything is ok in the passwords");
    }

    @Test
    void validateNewUserWithAlreadyUsedUsername() {
        UserSignUpDTO userToBeAdd = new UserSignUpDTO();
        userToBeAdd.setUsername("USER");
        userToBeAdd.setPassword("123456");
        userToBeAdd.setPasswordConfirm(userToBeAdd.getPassword());

        FreeSocialUser user = FreeSocialUser.of(userToBeAdd);

        //Username will exists in first attempt, then will not exists anymore
        when(userAuthenticationRepository.getByUsernameIgnoreCase(user.getAuthentication().getUsername()))
                .thenReturn(Optional.of(user.getAuthentication()))
                .thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> {
            userAuthenticationService.validateNewUser(user.getAuthentication());
        }, "Username already exists");

        assertDoesNotThrow(() -> {
            userAuthenticationService.validateNewUser(user.getAuthentication());
        }, "Username does not exists");
    }

    @Test
    void validateNewUserWithCorrectArguments() {
        UserSignUpDTO userToBeAdd = new UserSignUpDTO();
        userToBeAdd.setUsername("User");
        userToBeAdd.setPassword("123123");
        userToBeAdd.setPasswordConfirm(userToBeAdd.getPassword());

        //Username will not exists
        when(userAuthenticationRepository.getByUsernameIgnoreCase(any())).thenReturn(Optional.empty());

        assertDoesNotThrow(() -> {
            userAuthenticationService.validateNewUser(FreeSocialUser.of(userToBeAdd).getAuthentication());
        }, "User passwords are correct and username does not exists");
    }

}
