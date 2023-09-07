package com.freesocial.posts.service;

import com.freesocial.posts.common.exceptions.PostNotFoundException;
import com.freesocial.posts.common.util.PostUtil;
import com.freesocial.posts.entity.UserPostLike;
import com.freesocial.posts.entity.PostLikeCounter;
import com.freesocial.posts.repository.UserPostLikeRepository;
import com.freesocial.posts.repository.PostLikeCounterRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional(rollbackFor = Exception.class)
@Slf4j
public class PostLikeService {

    @Autowired
    private PostLikeCounterRepository postLikesRepository;

    @Autowired
    private UserPostLikeRepository postLikeRepository;

    @Autowired
    private PostUtil postUtil;

    @Autowired
    private TryPostLikeService tryPostLikeService;

    /**
     * Save new like or remove like of a post depending on if like already exists or not
     * If exists, will remove like and decrement, if not, will save a new like and increment
     *
     * @param postUuid identifies the post
     * @param userUuid identifies the user
     */
    public void like(String postUuid, String userUuid) {
        Optional<PostLikeCounter> likesOpt = postLikesRepository.findByPost_PostUuid(postUuid);
        PostLikeCounter postLikes = likesOpt.orElseThrow(() -> new PostNotFoundException());

        tryPostLikeService.tryLike(postLikes, userUuid);
    }

}
