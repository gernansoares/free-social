package com.freesocial.gateway.repository;

import com.freesocial.gateway.entity.UserAuthentication;
import com.freesocial.lib.repository.ReadOnlyRepository;

import java.util.Optional;

public interface UserAuthenticationRepository extends ReadOnlyRepository<UserAuthentication, Long> {

    Optional<UserAuthentication> findByUsernameIgnoreCase(String username);

}
