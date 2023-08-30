package com.freesocial.security.repository;

import com.freesocial.lib.repository.ReadOnlyRepository;
import com.freesocial.security.entity.UserAuthentication;

import java.util.Optional;

public interface UserAuthenticationRepository extends ReadOnlyRepository<UserAuthentication, Long> {

    Optional<UserAuthentication> findByUsernameIgnoreCase(String username);

}
