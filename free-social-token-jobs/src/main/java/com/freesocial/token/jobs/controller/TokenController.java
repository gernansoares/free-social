package com.freesocial.token.jobs.controller;

import com.freesocial.lib.config.kafka.KafkaTopicConfig;
import com.freesocial.token.jobs.service.UserTokenService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class TokenController {

    @Autowired
    private UserTokenService userTokenService;

    @KafkaListener(topics = KafkaTopicConfig.TOKEN_REGISTRATION_TOPIC, groupId = "1")
    public void listenTokenRegistration(String generatedToken) {
        log.info(String.format("Saving token %s", generatedToken));
        userTokenService.registerToken(generatedToken);
    }

    @KafkaListener(topics = KafkaTopicConfig.TOKEN_REMOVAL_TOPIC, groupId = "1")
    public void listenTokenRemoval(String tokenToRemove) {
        log.info(String.format("Removing token %s", tokenToRemove));
        userTokenService.removeToken(tokenToRemove);
    }

    @KafkaListener(topics = KafkaTopicConfig.DELETE_ALL_TOKENS_TOPIC, groupId = "1")
    public void listenAllTokenRemoval(String userUuid) {
        log.info(String.format("Removing all tokens of %s", userUuid));
        userTokenService.removeAllTokens(userUuid);
    }

}
