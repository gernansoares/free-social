package com.freesocial.posts.service;

import com.freesocial.lib.properties.ErrorUtil;
import com.freesocial.posts.common.util.Constants;
import com.freesocial.posts.common.util.PostUtil;
import com.freesocial.posts.entity.Post;
import com.freesocial.posts.repository.PostRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional(rollbackFor = Exception.class)
@Slf4j
public class PostService {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private PostContentService postContentService;

    @Autowired
    private PostUtil postUtil;

    /**
     * Creates a new post, saves the uploaded file if its exists
     *
     * @param post post to be saved
     * @param file upload belonging to the post
     * @return the recently added user with its own ID and UUID
     */
    public Post create(Post post, FilePart file) {
        postContentService.validatePostContent(post);
        postContentService.saveFile(post.getContent(), file);

        return postRepository.save(post);
    }

    /**
     * Deletes a post using its UUID, also delete its file (if exists)
     * will validate if post belongs to the user by UUIDs
     *
     * @param postUuid post to be deleted UUID
     * @param userUuid active user UUID
     */
    public void delete(String postUuid, String userUuid) {
        Optional<Post> postOpt = postRepository.findByPostUuid(postUuid);
        Post post = postOpt.orElseThrow(() -> new IllegalArgumentException(ErrorUtil.getMessage(Constants.POST_NOT_FOUND)));

        postUtil.validatePostBelongsToUser(post, userUuid);

        postContentService.deleteFile(post.getContent());
        postRepository.delete(post);
    }

}
