package com.freesocial.security.service;

import com.freesocial.lib.config.security.JwtUtil;
import com.freesocial.lib.properties.ErroUtil;
import com.freesocial.security.common.util.Constants;
import com.freesocial.security.entity.UserToken;
import com.freesocial.security.repository.UserTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional(rollbackFor = Exception.class)
public class UserTokenService {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserTokenRepository userTokenRepository;

    public UserToken registerToken(String generatedToken) {
        Optional<UserToken> tokenOpt = userTokenRepository.findByToken(generatedToken);
        tokenOpt.ifPresent(userToken -> {
            throw new IllegalArgumentException(ErroUtil.getMessage(Constants.TOKEN_ALREADY_EXISTS));
        });

        return userTokenRepository.save(new UserToken(generatedToken, jwtUtil.getUuidFromToken(generatedToken)));
    }

    public void removeToken(String tokenToRemove) {
        Optional<UserToken> tokenOpt = userTokenRepository.findByToken(tokenToRemove);
        tokenOpt.orElseThrow(() -> {
            throw new IllegalArgumentException(ErroUtil.getMessage(Constants.TOKEN_DOES_NOT_EXISTS));
        });

        userTokenRepository.delete(tokenOpt.get());
    }

    public void removeAllTokens(String userUuid) {
        userTokenRepository.deleteByUserUuid(userUuid);
    }

}
