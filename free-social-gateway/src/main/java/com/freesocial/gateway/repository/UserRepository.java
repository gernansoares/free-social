package com.freesocial.gateway.repository;

import com.freesocial.gateway.entity.FreeSocialUser;
import com.freesocial.lib.repository.ReadOnlyRepository;

import java.util.Optional;

public interface UserRepository extends ReadOnlyRepository<FreeSocialUser, Long> {

    Optional<FreeSocialUser> findByUuid(String uuid);

}
