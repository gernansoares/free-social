package com.freesocial.security.service;

import com.freesocial.lib.config.tests.BasicTest;
import com.freesocial.security.repository.UserAuthenticationRepository;
import com.freesocial.security.dto.AuthRequest;
import com.freesocial.security.entity.FreeSocialUser;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.test.StepVerifier;

import java.util.Optional;

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

        StepVerifier.create(userAuthenticationService.login(new AuthRequest(USERNAME, "")))
                .expectNextCount(0)
                .expectComplete().verify();
    }

    @Test
    void loginWithDisabledUser() {
        FreeSocialUser novo = FreeSocialUser.of(USERNAME, "", false);

        //User will be disabled
        when(userAuthenticationRepository.findByUsernameIgnoreCase(Mockito.any(String.class)))
                .thenReturn(Optional.of(novo.getAuthentication()));

        StepVerifier.create(userAuthenticationService.login(new AuthRequest(USERNAME, "")))
                .expectNextCount(0)
                .expectComplete().verify();
    }

    @Test
    void loginWithCorrectUser() {
        FreeSocialUser novo = FreeSocialUser.of(USERNAME,
                "$2a$10$q.K3vE8WQEJKzDgnWBfe7O5Zwl2e1lz8UulpsBUlqYlcFa.Wa4NRG", true);

        //User will be found and will be enabled
        when(userAuthenticationRepository.findByUsernameIgnoreCase(Mockito.any(String.class)))
                .thenReturn(Optional.of(novo.getAuthentication()));

        StepVerifier.create(userAuthenticationService.login(new AuthRequest(USERNAME, "123123")))
                .expectNextCount(1)
                .expectComplete().verify();
    }

}
