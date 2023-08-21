package com.freesocial.gateway.controller;

import com.freesocial.gateway.dto.AuthRequest;
import com.freesocial.gateway.dto.AuthResponse;
import com.freesocial.gateway.service.UserAuthenticationService;
import com.freesocial.lib.config.kafka.KafkaTopicConfig;
import com.freesocial.lib.config.security.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/logout")
@Slf4j
public class LogoutController {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @PostMapping("/logout")
    @ResponseStatus(code = HttpStatus.OK)
    @Operation(summary = "Provides user logout by sending token for removal")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Token checked and send to removal")
    })
    public Mono<ResponseEntity> logout(Authentication authentication) {
        log.info(String.format("User %s logging out", authentication.getName()));
        kafkaTemplate.send(KafkaTopicConfig.TOKEN_REMOVAL_TOPIC, authentication.getCredentials().toString());
        return Mono.just(ResponseEntity.ok(""));
    }

}
