package com.freesocial.posts.repository;

import com.freesocial.posts.entity.PostLike;
import com.freesocial.posts.entity.PostLikes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PostLikeRepository extends JpaRepository<PostLike, Long> {

    Optional<PostLike> findByPost_PostUuidAndUserUuid(String postUuid, String userUuid);

}
