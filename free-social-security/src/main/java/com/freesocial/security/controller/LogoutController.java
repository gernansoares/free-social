package com.freesocial.security.controller;

import com.freesocial.security.service.UserTokenService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/logout")
@Slf4j
public class LogoutController {

    @Autowired
    private UserTokenService userTokenService;

    @PostMapping("/logout")
    @ResponseStatus(code = HttpStatus.OK)
    @Operation(summary = "Provides user logout by sending token for removal")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Token checked and send to removal"),
            @ApiResponse(responseCode = "401", description = "User unauthorized")
    })
    public Mono<ResponseEntity> logout(Authentication authentication) {
        log.info(String.format("User %s logging out", authentication.getName()));
        userTokenService.removeToken(authentication.getCredentials().toString());
        return Mono.just(ResponseEntity.ok(""));
    }

}
