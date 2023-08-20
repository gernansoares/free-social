package com.freesocial.gateway.service;

import com.freesocial.gateway.entity.FreeSocialUser;
import com.freesocial.gateway.repository.UserAuthenticationRepository;
import com.freesocial.lib.config.exceptions.DisabledException;
import com.freesocial.lib.config.exceptions.UserNotFoundException;
import com.freesocial.lib.config.tests.BasicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.cloud.gateway.filter.factory.SaveSessionGatewayFilterFactory;
import org.springframework.cloud.gateway.filter.factory.TokenRelayGatewayFilterFactory;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserAuthenticationServiceTests extends BasicTest {

    @Mock
    private UserAuthenticationRepository userAuthenticationRepository;

    @InjectMocks
    private UserAuthenticationService userAuthenticationService;

    private final String USERNAME = "usertest";

    @Test
    void loginWithNoExistingUser() {
        //Will not find any user
        when(userAuthenticationRepository.findByUsernameIgnoreCase(Mockito.any(String.class)))
                .thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> {
            userAuthenticationService.findByUsername(USERNAME);
        }, "User does not exists");
    }

    @Test
    void loginWithDisabledUser() {
        FreeSocialUser novo = FreeSocialUser.of(USERNAME, "", false);

        //User will be disabled
        when(userAuthenticationRepository.findByUsernameIgnoreCase(Mockito.any(String.class)))
                .thenReturn(Optional.of(novo.getAuthentication()));

        assertThrows(DisabledException.class, () -> {
            userAuthenticationService.findByUsername(USERNAME);
        }, "User is disabled");
    }

    @Test
    void loginWithCorrectUser() {
        FreeSocialUser novo = FreeSocialUser.of("", "", true);

        //User will be disabled
        when(userAuthenticationRepository.findByUsernameIgnoreCase(Mockito.any(String.class)))
                .thenReturn(Optional.of(novo.getAuthentication()));

        assertDoesNotThrow(() -> {
            userAuthenticationService.findByUsername(USERNAME);
        }, "User exists and is enabled");
    }

}
