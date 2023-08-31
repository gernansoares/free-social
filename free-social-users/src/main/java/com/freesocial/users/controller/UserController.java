package com.freesocial.users.controller;

import com.freesocial.lib.config.security.JwtAuthenticationFilter;
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
    @Operation(summary = "Update user's profile, UUID identifies the user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Profile updated"),
            @ApiResponse(responseCode = "400", description = "Invalid information"),
    })
    public void updateProfile(@RequestHeader(JwtAuthenticationFilter.HEADER_UUID) String uuid,
                              @RequestBody @Valid UserProfileDTO profileDto) {
        log.info(String.format("Updating user with UUID %s profile", uuid));
        userProfileService.update(profileDto, uuid);
        log.info(String.format("User with UUID %s profile updated successfully", uuid));
    }

    @PutMapping("/password")
    @ResponseStatus(code = HttpStatus.OK)
    @Operation(summary = "Update user's username and password, UUID identifies the user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Password updated"),
            @ApiResponse(responseCode = "400", description = "Invalid information"),
    })
    public void updateAuthentication(@RequestHeader(JwtAuthenticationFilter.HEADER_UUID) String uuid,
                               @RequestBody @Valid UserAuthenticationDTO passwordDto) {
        log.info(String.format("Updating user with UUID %s password", uuid));
        userAuthenticationService.update(passwordDto, uuid);
        log.info(String.format("User with UUID %s password updated successfully", uuid));
    }

    @DeleteMapping
    @ResponseStatus(code = HttpStatus.OK)
    @Operation(summary = "Delete user and its dependencies, UUID identifies the user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User deleted"),
            @ApiResponse(responseCode = "400", description = "Invalid information"),
    })
    public void delete(@RequestHeader(JwtAuthenticationFilter.HEADER_UUID) String uuid) {
        log.info(String.format("Deleting user with UUID %s", uuid));
        userService.delete(uuid);
        log.info(String.format("User with UUID %s deleted successfully", uuid));
    }

}
