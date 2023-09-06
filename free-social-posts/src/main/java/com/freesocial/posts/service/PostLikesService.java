package com.freesocial.posts.service;

import com.freesocial.lib.config.exceptions.FileUploadException;
import com.freesocial.lib.properties.ErrorUtil;
import com.freesocial.posts.common.enums.ValidExtensions;
import com.freesocial.posts.common.exceptions.PostNotFoundException;
import com.freesocial.posts.common.util.Constants;
import com.freesocial.posts.common.util.PostUtil;
import com.freesocial.posts.dto.PostDTO;
import com.freesocial.posts.entity.Post;
import com.freesocial.posts.entity.PostContent;
import com.freesocial.posts.entity.PostLike;
import com.freesocial.posts.entity.PostLikes;
import com.freesocial.posts.repository.PostLikeRepository;
import com.freesocial.posts.repository.PostLikesRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.assertj.core.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional(rollbackFor = Exception.class)
@Slf4j
public class PostLikesService {

    @Autowired
    private PostLikesRepository postLikesRepository;

    @Autowired
    private PostLikeRepository postLikeRepository;

    @Autowired
    private PostUtil postUtil;

    public void like(String postUuid, String userUuid) {
        Optional<PostLikes> likesOpt = postLikesRepository.findByPost_PostUuid(postUuid);
        PostLikes postLikes = likesOpt.orElseThrow(() -> new PostNotFoundException());

        Optional<PostLike> likeOpt = postLikeRepository.findByPost_PostUuidAndUserUuid(postLikes.getPost().getPostUuid(), userUuid);
        postLikes.incrementOrDecrement(!likeOpt.isPresent());
        postLikesRepository.save(postLikes);

        likeOpt.ifPresentOrElse((like) -> {
            postLikeRepository.delete(like);
        }, () -> {
            postLikeRepository.save(PostLike.create(postLikes.getPost(), userUuid));
        });
    }

}
