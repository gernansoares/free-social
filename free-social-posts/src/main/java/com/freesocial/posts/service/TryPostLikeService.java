package com.freesocial.posts.service;

import com.freesocial.posts.common.exceptions.PostNotFoundException;
import com.freesocial.posts.common.util.PostUtil;
import com.freesocial.posts.entity.PostLikeCounter;
import com.freesocial.posts.entity.UserPostLike;
import com.freesocial.posts.repository.PostLikeCounterRepository;
import com.freesocial.posts.repository.UserPostLikeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.retry.RetryPolicy;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Slf4j
public class TryPostLikeService {

    @Autowired
    private PostLikeCounterRepository postLikesRepository;

    @Autowired
    private UserPostLikeRepository postLikeRepository;

    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
    @Retryable(retryFor = ObjectOptimisticLockingFailureException.class, maxAttempts = 10)
    public void tryLike(String postUuid, String userUuid) {
        Optional<PostLikeCounter> likesOpt = postLikesRepository.findByPost_PostUuid(postUuid);
        PostLikeCounter postLikes = likesOpt.orElseThrow(() -> new PostNotFoundException());

        Optional<UserPostLike> likeOpt = postLikeRepository.findByPost_PostUuidAndUserUuid(postLikes.getPost().getPostUuid(), userUuid);
        postLikes.incrementOrDecrement(!likeOpt.isPresent());
        postLikesRepository.save(postLikes);

        likeOpt.ifPresentOrElse((like) -> {
            postLikeRepository.delete(like);
        }, () -> {
            postLikeRepository.save(UserPostLike.create(postLikes.getPost(), userUuid));
        });
    }

}
