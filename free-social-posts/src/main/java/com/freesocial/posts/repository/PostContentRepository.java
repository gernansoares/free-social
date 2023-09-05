package com.freesocial.posts.repository;

import com.freesocial.posts.entity.Post;
import com.freesocial.posts.entity.PostContent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostContentRepository extends JpaRepository<PostContent, Long> {

    Optional<PostContent> findByPost_PostUuid(String uuid);

}
