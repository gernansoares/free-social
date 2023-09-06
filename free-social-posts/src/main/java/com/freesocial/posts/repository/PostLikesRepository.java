package com.freesocial.posts.repository;

import com.freesocial.posts.entity.PostContent;
import com.freesocial.posts.entity.PostLikes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PostLikesRepository extends JpaRepository<PostLikes, Long> {

    Optional<PostLikes> findByPost_PostUuid(String uuid);

}
