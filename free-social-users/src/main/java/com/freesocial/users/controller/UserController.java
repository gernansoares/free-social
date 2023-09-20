package com.freesocial.users.controller;

import com.freesocial.lib.config.GlobalContants;
import com.freesocial.users.dto.UserAuthenticationDTO;
import com.freesocial.users.dto.UserProfileDTO;
import com.freesocial.users.service.UserAuthenticationService;
import com.freesocial.users.service.UserProfileService;
import com.freesocial.users.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import java.util.stream.Collectors;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {

    @Autowired
    private UserProfileService userProfileService;

    @Autowired
    private UserAuthenticationService userAuthenticationService;

    @Autowired
    private UserService userService;

    @PutMapping("/profile")
    @ResponseStatus(code = HttpStatus.OK)
    @Operation(summary = "Update user's profile")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Profile updated"),
            @ApiResponse(responseCode = "400", description = "User not found"),
            @ApiResponse(responseCode = "401", description = "User unauthorized")
    })
    public void updateProfile(@RequestHeader(GlobalContants.HEADER_UUID) String userUuid,
                              @RequestBody @Valid UserProfileDTO profileDto) {
        log.info(String.format("Updating user with UUID %s profile", userUuid));
        userProfileService.update(profileDto, userUuid);
        log.info(String.format("User with UUID %s profile updated successfully", userUuid));
    }

    @PutMapping("/authentication")
    @ResponseStatus(code = HttpStatus.OK)
    @Operation(summary = "Update user's username and password")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Password updated"),
            @ApiResponse(responseCode = "400", description = "User not found | Password/confirmation does not match | Username already in use"),
            @ApiResponse(responseCode = "401", description = "User unauthorized")
    })
    public void updateAuthentication(@RequestHeader(GlobalContants.HEADER_UUID) String userUuid,
                                     @RequestBody @Valid UserAuthenticationDTO authenticationDto) {
        log.info(String.format("Updating user with UUID %s authentication", userUuid));
        userAuthenticationService.update(authenticationDto, userUuid);
        log.info(String.format("User with UUID %s authentication updated successfully", userUuid));
    }

    @DeleteMapping
    @ResponseStatus(code = HttpStatus.OK)
    @Operation(summary = "Delete a user and its dependencies")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User deleted"),
            @ApiResponse(responseCode = "400", description = "User not found"),
            @ApiResponse(responseCode = "401", description = "User unauthorized")
    })
    public void delete(@RequestHeader(GlobalContants.HEADER_UUID) String userUuid) {
        log.info(String.format("Deleting user with UUID %s", userUuid));
        userService.delete(userUuid);
        log.info(String.format("User with UUID %s deleted successfully", userUuid));
    }

}
