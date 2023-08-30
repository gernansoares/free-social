package com.freesocial.security.controller;

import com.freesocial.lib.config.kafka.KafkaTopicConfig;
import com.freesocial.security.service.UserTokenService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class TokenController {

    @Autowired
    private UserTokenService userTokenService;

    @KafkaListener(topics = KafkaTopicConfig.DELETE_ALL_TOKENS_TOPIC, groupId = "1")
    public void listenAllTokensRemove(String userUuid) {
        log.info(String.format("Removing all tokens of %s", userUuid));
        userTokenService.removeAllTokens(userUuid);
    }

}
