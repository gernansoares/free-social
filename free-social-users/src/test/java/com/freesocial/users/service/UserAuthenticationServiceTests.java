package com.freesocial.users.service;

import com.freesocial.lib.config.exceptions.UserNotFoundException;
import com.freesocial.lib.config.exceptions.UsernameAlreadyExistsException;
import com.freesocial.lib.config.tests.BasicTest;
import com.freesocial.users.common.util.UserUtils;
import com.freesocial.users.dto.UserAuthenticationDTO;
import com.freesocial.users.dto.UserSignUpDTO;
import com.freesocial.users.entity.FreeSocialUser;
import com.freesocial.users.entity.UserAuthentication;
import com.freesocial.users.repository.UserAuthenticationRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserAuthenticationServiceTests extends BasicTest {

    @Mock
    private UserAuthenticationRepository userAuthenticationRepository;

    @InjectMocks
    private UserAuthenticationService userAuthenticationService;

    @BeforeAll
    static void setup() {
        new UserUtils().setEncoder(new BCryptPasswordEncoder());
    }

    @Test
    void validateNewUserWithIncorrectPasswords() {
        UserSignUpDTO userToBeAdd = new UserSignUpDTO();
        userToBeAdd.setUsername("");
        userToBeAdd.setPassword("123456");
        userToBeAdd.setPasswordConfirm("");

        FreeSocialUser user = FreeSocialUser.of(userToBeAdd);

        //Username will not exists
        when(userAuthenticationRepository.findByUsernameIgnoreCase(user.getAuthentication().getUsername()))
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

        //Username will exists in first attempt, then will not exist anymore
        when(userAuthenticationRepository.findByUsernameIgnoreCase(user.getAuthentication().getUsername()))
                .thenReturn(Optional.of(user.getAuthentication()))
                .thenReturn(Optional.empty());

        assertThrows(UsernameAlreadyExistsException.class, () -> {
            userAuthenticationService.validateNewUser(user.getAuthentication());
        }, "Username already exists");

        assertDoesNotThrow(() -> {
            userAuthenticationService.validateNewUser(user.getAuthentication());
        }, "Username does not exists");
    }

    @Test
    void validateNewUserWithCorrectArguments() {
        UserSignUpDTO userToBeAdd = new UserSignUpDTO();
        userToBeAdd.setUsername("New user");
        userToBeAdd.setPassword("123123");
        userToBeAdd.setPasswordConfirm(userToBeAdd.getPassword());

        //Username will not exists
        when(userAuthenticationRepository.findByUsernameIgnoreCase(any())).thenReturn(Optional.empty());

        assertDoesNotThrow(() -> {
            userAuthenticationService.validateNewUser(FreeSocialUser.of(userToBeAdd).getAuthentication());
        }, "User passwords are correct and username does not exists");
    }


    @Test
    void validateUpdateAuthenticationWithIncorrectPasswords() {
        UserAuthenticationDTO authentication = new UserAuthenticationDTO();
        authentication.setUsername("");
        authentication.setPassword("123456");
        authentication.setPasswordConfirm("");

        //Username will not exists
        when(userAuthenticationRepository.findByUser_Uuid(any())).thenReturn(Optional.of(new UserAuthentication()));

        assertThrows(IllegalArgumentException.class, () -> {
            userAuthenticationService.update(authentication, "");
        }, "User password and confirmation are not the same");
    }

    @Test
    void validateUpdateAuthenticationWithNoExistingUser() {
        UserAuthenticationDTO authentication = new UserAuthenticationDTO();
        authentication.setUsername("Jhonny");
        authentication.setPassword("123456");
        authentication.setPasswordConfirm(authentication.getPassword());

        //User must not exists
        when(userAuthenticationRepository.findByUser_Uuid(Mockito.any()))
                .thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> {
            userAuthenticationService.update(authentication, "");
        }, "User does not exists");
    }

    @Test
    void validateUpdateAuthenticationWithAlreadyExistingUsername() {
        UserAuthenticationDTO authenticationDto = new UserAuthenticationDTO();
        authenticationDto.setUsername("New user");
        authenticationDto.setPassword("123456");
        authenticationDto.setPasswordConfirm(authenticationDto.getPassword());

        UserAuthentication conflictingAuth = new UserAuthentication();
        conflictingAuth.setUsername(UserUtils.prepareUsername(authenticationDto.getUsername()));

        UserAuthentication updatingAuth = new UserAuthentication();
        updatingAuth.setUsername(UserUtils.prepareUsername("old user name"));

        //User must exists
        when(userAuthenticationRepository.findByUser_Uuid(any()))
                .thenReturn(Optional.of(updatingAuth));

        //Username will exists in first attempt, then will not exist anymore
        when(userAuthenticationRepository.findByUsernameIgnoreCase(Mockito.any()))
                .thenReturn(Optional.of(conflictingAuth))
                .thenReturn(Optional.of(updatingAuth));

        assertThrows(UsernameAlreadyExistsException.class, () -> {
            userAuthenticationService.update(authenticationDto, any());
        }, "Username must be in use");

        assertDoesNotThrow(() -> userAuthenticationService.update(authenticationDto, any()),
                "Username does not exists, everything is ok");
    }

    @Test
    void validateUpdateAuthenticationWithCorrectInformation() {
        UserAuthenticationDTO authentication = new UserAuthenticationDTO();
        authentication.setUsername("User tests");
        authentication.setPassword("123456");
        authentication.setPasswordConfirm(authentication.getPassword());

        String uuid = "123";

        //User must exists
        when(userAuthenticationRepository.findByUser_Uuid(uuid))
                .thenReturn(Optional.of(new UserAuthentication()));

        //Username will not exists
        when(userAuthenticationRepository.findByUsernameIgnoreCase(Mockito.any()))
                .thenReturn(Optional.empty());

        when(userAuthenticationRepository.save(Mockito.any())).thenReturn(new UserAuthentication());

        assertDoesNotThrow(() -> userAuthenticationService.update(authentication, uuid),
                "User exists, passwords matches and username is available");
    }

}
