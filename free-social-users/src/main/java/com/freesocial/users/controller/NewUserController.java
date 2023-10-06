package com.freesocial.users.controller;

import com.freesocial.users.dto.UserDTO;
import com.freesocial.users.dto.UserSignUpDTO;
import com.freesocial.users.entity.FreeSocialUser;
import com.freesocial.users.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.WebSession;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/newuser")
@Slf4j
public class NewUserController {

    @Autowired
    private UserService userService;

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    @Operation(summary = "Creates a new user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User created"),
            @ApiResponse(responseCode = "400", description = "Password/confirmation does not match | Username already in use"),
    })
    public Mono<UserDTO> create(@RequestBody @Valid UserSignUpDTO userDto) {
        FreeSocialUser user = FreeSocialUser.of(userDto);
        log.info(String.format("Adding user %s with UUID", userDto.getUsername(), user.getUuid()));
        user = userService.create(FreeSocialUser.of(userDto));
        log.info(String.format("User %s added successfully with UUID %s", userDto.getUsername(), user.getUuid()));
        return Mono.just(UserDTO.of(user));
    }

}
