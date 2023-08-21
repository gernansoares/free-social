package com.freesocial.gateway.repository;

import com.freesocial.gateway.entity.FreeSocialUser;
import com.freesocial.gateway.entity.UserToken;
import com.freesocial.lib.repository.ReadOnlyRepository;

import java.util.Optional;

public interface UserTokenRepository extends ReadOnlyRepository<UserToken, Long> {

    Optional<UserToken> findByToken(String token);

}
