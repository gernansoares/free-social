package com.freesocial.users.repository;

import com.freesocial.users.entity.UserAuthentication;
import com.freesocial.users.entity.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserAuthenticationRepository extends JpaRepository<UserAuthentication, Long> {

    Optional<UserAuthentication> findByUsernameIgnoreCase(String username);

    Optional<UserAuthentication> findByUser_Uuid(String uuid);

    @Modifying
    @Query(nativeQuery = true,
            value = "UPDATE user_authentication u SET u.username = :username, u.password = :password " +
                    "WHERE u.user_id = (SELECT id FROM free_social_user WHERE uuid = :userUuid)")
    void updateUsernameAndPasswordByUserUuid(@Param("username") String username,
                                             @Param("password") String password,
                                             @Param("userUuid") String userUuid);

}
