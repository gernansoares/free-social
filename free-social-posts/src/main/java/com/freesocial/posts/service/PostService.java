package com.freesocial.posts.service;

import com.freesocial.lib.config.exceptions.FileUploadException;
import com.freesocial.lib.properties.ErroUtil;
import com.freesocial.posts.common.enums.ValidExtensions;
import com.freesocial.posts.common.util.Constants;
import com.freesocial.posts.entity.Post;
import com.freesocial.posts.repository.PostContentRepository;
import com.freesocial.posts.repository.PostRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.assertj.core.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional(rollbackFor = Exception.class)
@Slf4j
public class PostService {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private PostContentService postContentService;

    @Autowired
    private PostService postService;

    /**
     * Validate if a solicited object UUID owner is equals to the active user UUID
     *
     * @param post     post being validated
     * @param userUuid active user UUID
     */
    public void validatePostBelongsToUser(Post post, String userUuid) {
        if (!post.getUserUuid().equals(userUuid)) {
            throw new IllegalArgumentException(ErroUtil.getMessage(Constants.INVALID_POST));
        }
    }

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
        Post post = postOpt.orElseThrow(() -> new IllegalArgumentException(ErroUtil.getMessage(Constants.POST_NOT_FOUND)));

        validatePostBelongsToUser(post, userUuid);
        postContentService.deleteFile(post.getContent());
        postRepository.delete(post);
    }

}