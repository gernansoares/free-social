package com.freesocial.users.repository;

import com.freesocial.users.entity.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserProfileRepository extends JpaRepository<UserProfile, Long> {

    Optional<UserProfile> findByUser_Uuid(String uuid);

    @Modifying
    @Query(nativeQuery = true,
            value = "UPDATE user_profile SET name = :name, bio = :bio " +
                    "WHERE user_id = (SELECT id FROM free_social_user WHERE uuid = :userUuid)")
    void updateNameAndBioByUserUuid(@Param("name") String name,
                                    @Param("bio") String bio,
                                    @Param("userUuid") String userUuid);

}
