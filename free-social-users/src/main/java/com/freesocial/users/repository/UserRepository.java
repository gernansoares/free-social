package com.freesocial.users.repository;

import com.freesocial.users.entity.FreeSocialUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<FreeSocialUser, Long> {

    Optional<FreeSocialUser> findByUuid(String uuid);

}
