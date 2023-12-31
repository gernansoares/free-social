package com.freesocial.security.controller;

import com.freesocial.lib.config.security.jwt.JwtUtil;
import com.freesocial.security.dto.AuthRequest;
import com.freesocial.security.dto.AuthResponse;
import com.freesocial.security.service.UserAuthenticationService;
import com.freesocial.security.service.UserTokenService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/auth")
@Slf4j
public class AuthenticationController {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserAuthenticationService userAuthenticationService;

    @Autowired
    private UserTokenService userTokenService;

    @PostMapping("/login")
    @ResponseStatus(code = HttpStatus.OK)
    @Operation(summary = "Provides user authentication by checking username and password")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Information is correct, a new token is generated"),
            @ApiResponse(responseCode = "401", description = "Incorrect login information"),
    })
    public Mono<ResponseEntity<AuthResponse>> login(@RequestBody @Valid AuthRequest ar) {
        log.info(String.format("Logging user with username %s", ar.getUsername()));

        return userAuthenticationService.login(ar)
                .flatMap(userDetails -> {
                    String token = jwtUtil.generateToken(userDetails);
                    userTokenService.registerToken(token);
                    log.info(String.format("User %s logged in", ar.getUsername()));
                    return Mono.just(ResponseEntity.ok(new AuthResponse(token)));
                })
                .switchIfEmpty(Mono.defer(() -> {
                    log.info(String.format("Logging user %s failed", ar.getUsername()));
                    return Mono.just(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
                }));
    }

}
