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
            @ApiResponse(responseCode = "201", description = "Created user"),
            @ApiResponse(responseCode = "400", description = "Invalid information"),
    })
    public UserDTO createUser(@RequestBody @Valid UserSignUpDTO userDto) {
        log.info(String.format("Adding user %s", userDto.getName()));
        FreeSocialUser user = userService.create(FreeSocialUser.of(userDto));
        log.info(String.format("User %s added successfully with UUID %s", userDto.getName(), user.getUuid()));
        return UserDTO.of(user);
    }

}
