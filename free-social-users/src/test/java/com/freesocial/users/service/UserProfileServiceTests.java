package com.freesocial.users.service;

import com.freesocial.lib.config.exceptions.UserNotFoundException;
import com.freesocial.lib.config.tests.BasicTest;
import com.freesocial.users.dto.UserProfileDTO;
import com.freesocial.users.entity.UserProfile;
import com.freesocial.users.repository.UserProfileRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserProfileServiceTests extends BasicTest {

    @Mock
    private UserProfileRepository userProfileRepository;

    @InjectMocks
    private UserProfileService userProfileService;

    @Test
    void validateUpdateWithCorrectArguments() {
        UserProfileDTO profile = new UserProfileDTO();
        profile.setName("Hello");
        profile.setBio("New here");

        //Username will exists
        when(userProfileRepository.findByUser_Uuid(any())).thenReturn(Optional.of(new UserProfile()));

        assertDoesNotThrow(() -> {
            userProfileService.update(profile, "");
        }, "User exists");
    }

    @Test
    void validateUpdateWithNoExistingUser() {
        UserProfileDTO profile = new UserProfileDTO();
        profile.setName("Tests");
        profile.setBio("");

        //User will not exists
        when(userProfileRepository.findByUser_Uuid(Mockito.any()))
                .thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> {
            userProfileService.update(profile, "");
        }, "User does not exists");
    }

}
