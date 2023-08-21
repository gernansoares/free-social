package com.freesocial.token.jobs.service;

import com.freesocial.lib.config.security.JwtUtil;
import com.freesocial.lib.properties.ErroUtil;
import com.freesocial.token.jobs.common.util.Constants;
import com.freesocial.token.jobs.entity.UserToken;
import com.freesocial.token.jobs.repository.UserTokenRepository;
import org.antlr.v4.runtime.Token;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserTokenService {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserTokenRepository userTokenRepository;

    public UserToken registarToken(String generatedToken) {
        Optional<UserToken> tokenOpt = userTokenRepository.findByToken(generatedToken);
        tokenOpt.ifPresent(userToken -> {
            throw new IllegalArgumentException(ErroUtil.getMessage(Constants.TOKEN_ALREADY_EXISTS));
        });

        return userTokenRepository.save(new UserToken(generatedToken, jwtUtil.getUuidFromToken(generatedToken)));
    }

    public void removerToken(String tokenToRemove) {
        Optional<UserToken> tokenOpt = userTokenRepository.findByToken(tokenToRemove);
        tokenOpt.orElseThrow(() -> {
            throw new IllegalArgumentException(ErroUtil.getMessage(Constants.TOKEN_DOES_NOT_EXISTS));
        });

        userTokenRepository.delete(tokenOpt.get());
    }

}
