package com.freesocial.gateway.controller;

import com.freesocial.gateway.dto.AuthRequest;
import com.freesocial.gateway.dto.AuthResponse;
import com.freesocial.gateway.service.UserLoginService;
import com.freesocial.lib.config.security.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(value = "/auth")
public class AuthenticationController {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private UserLoginService userService;

    @PostMapping(value = "/login")
    @ResponseStatus(code = HttpStatus.OK)
    @Operation(summary = "Provides user authentication by checking username and password")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Information is correct, a new token is generated and returned"),
            @ApiResponse(responseCode = "401", description = "Incorrect login information"),
    })
    public Mono<ResponseEntity<AuthResponse>> login(@RequestBody @Valid AuthRequest ar) {
        return userService.findByUsername(ar.getUsername())
                .filter(userDetails -> BCrypt.checkpw(ar.getPassword(), userDetails.getPassword()))
                .map(userDetails -> ResponseEntity.ok(new AuthResponse(jwtUtil.generateToken(userDetails))))
                .switchIfEmpty(Mono.just(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()));
    }

}
