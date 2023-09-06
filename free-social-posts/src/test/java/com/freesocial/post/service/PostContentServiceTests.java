package com.freesocial.post.service;

import com.freesocial.lib.config.tests.BasicTest;
import com.freesocial.posts.FreeSocialPostsApplication;
import com.freesocial.posts.common.exceptions.PostNotFoundException;
import com.freesocial.posts.dto.PostDTO;
import com.freesocial.posts.entity.PostContent;
import com.freesocial.posts.repository.PostContentRepository;
import com.freesocial.posts.service.PostContentService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = FreeSocialPostsApplication.class)
class PostContentServiceTests extends BasicTest {

    @MockBean
    private PostContentRepository postContentRepository;

    @Autowired
    private PostContentService postContentService;

    @Test
    void validateUpdateWithCorrectArguments() {
        PostDTO postDto = new PostDTO();
        postDto.setText("Hello there");

        //Post UUID will exists
        when(postContentRepository.findByPost_PostUuid(any())).thenReturn(Optional.of(new PostContent()));

        assertDoesNotThrow(() -> {
            postContentService.update(postDto, "postUuid", "userUuid");
        }, "Post exists");
    }

    @Test
    void validateUpdateWithNoExistingPost() {
        PostDTO postDto = new PostDTO();
        postDto.setText("Tests");

        //Post will not exists
        when(postContentRepository.findByPost_PostUuid(Mockito.any()))
                .thenReturn(Optional.empty());

        assertThrows(PostNotFoundException.class, () -> {
            postContentService.update(postDto, "postUuid", "userUuid");
        }, "User does not exists");
    }

}
