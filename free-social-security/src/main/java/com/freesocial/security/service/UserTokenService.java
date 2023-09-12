package com.freesocial.security.service;

import com.freesocial.lib.config.security.jwt.JwtUtil;
import com.freesocial.lib.properties.ErrorUtil;
import com.freesocial.security.common.util.Constants;
import com.freesocial.security.entity.UserToken;
import com.freesocial.security.repository.UserTokenRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional(rollbackFor = Exception.class)
@Slf4j
public class UserTokenService {

    private final String DELETE_ALL_TOKENS_TOPIC = "delete-all-tokens";

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserTokenRepository userTokenRepository;

    /**
     * For each
     *
     * @param generatedToken
     * @return
     */
    public UserToken registerToken(String generatedToken) {
        Optional<UserToken> tokenOpt = userTokenRepository.findByToken(generatedToken);
        tokenOpt.ifPresent(userToken -> {
            throw new IllegalArgumentException(ErrorUtil.getMessage(Constants.TOKEN_ALREADY_EXISTS));
        });

        return userTokenRepository.save(new UserToken(generatedToken, jwtUtil.getUuidFromToken(generatedToken)));
    }

    public void validateToken(String token) {
        userTokenRepository.findByToken(token).orElseThrow(() -> {
            throw new IllegalArgumentException(ErrorUtil.getMessage(Constants.TOKEN_DOES_NOT_EXISTS));
        });
    }

    public void removeToken(String tokenToRemove) {
        Optional<UserToken> tokenOpt = userTokenRepository.findByToken(tokenToRemove);
        tokenOpt.orElseThrow(() -> {
            throw new IllegalArgumentException(ErrorUtil.getMessage(Constants.TOKEN_DOES_NOT_EXISTS));
        });

        userTokenRepository.delete(tokenOpt.get());
    }

    /**
     * Listens for delete all topics message
     *
     * @param userUuid user's UUID
     */
    @KafkaListener(topics = DELETE_ALL_TOKENS_TOPIC, groupId = "1")
    public void removeAllTokens(String userUuid) {
        log.info(String.format("Removing all tokens of %s", userUuid));
        userTokenRepository.deleteByUserUuid(userUuid);
    }

}
