package com.freesocial.security.controller;

import com.freesocial.security.service.UserTokenService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/token")
@Slf4j
public class TokenController {

    @Autowired
    private UserTokenService userTokenService;

    @GetMapping("{token}")
    @ResponseStatus(code = HttpStatus.OK)
    @Operation(summary = "Provides token validation")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Token found and valid"),
            @ApiResponse(responseCode = "400", description = "Token not found or invalid")
    })
    public Mono<ResponseEntity> validate(@PathVariable String token) {
        log.info(String.format("Validating token %s", token));
        userTokenService.validateToken(token);
        return Mono.just(ResponseEntity.ok(""));
    }

}
