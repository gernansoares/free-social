package com.freesocial.security.repository;

import com.freesocial.security.entity.UserToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserTokenRepository extends JpaRepository<UserToken, Long> {

    Optional<UserToken> findByToken(String token);

    void deleteByUserUuid(String userUuid);

}
