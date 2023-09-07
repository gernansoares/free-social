package com.freesocial.posts.repository;

import com.freesocial.posts.entity.UserPostLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserPostLikeRepository extends JpaRepository<UserPostLike, Long> {

    Optional<UserPostLike> findByPost_PostUuidAndUserUuid(String postUuid, String userUuid);

}
