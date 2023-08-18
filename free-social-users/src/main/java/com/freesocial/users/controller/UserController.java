package com.freesocial.users.controller;

import com.freesocial.users.dto.UserProfileDTO;
import com.freesocial.users.service.UserProfileService;
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

    @PutMapping
    @ResponseStatus(code = HttpStatus.OK)
    @Operation(summary = "Update user's profile, UUID identifies the user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User updated"),
            @ApiResponse(responseCode = "400", description = "Invalid information"),
    })
    public void updateProfile(@RequestHeader String uuid,
                              @RequestBody @Valid UserProfileDTO profileDto) {
        log.info(String.format("Updating user with UUID %s profile", uuid));
        userProfileService.update(profileDto, uuid);
        log.info(String.format("User with UUID %s profile updated successfully", uuid));
    }

}