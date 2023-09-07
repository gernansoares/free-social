package com.freesocial.posts.repository;

import com.freesocial.posts.entity.PostLikeCounter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PostLikeCounterRepository extends JpaRepository<PostLikeCounter, Long> {

    Optional<PostLikeCounter> findByPost_PostUuid(String uuid);

}
