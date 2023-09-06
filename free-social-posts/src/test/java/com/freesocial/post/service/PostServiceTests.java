package com.freesocial.post.service;

import com.freesocial.lib.config.tests.BasicTest;
import com.freesocial.posts.FreeSocialPostsApplication;
import com.freesocial.posts.dto.PostDTO;
import com.freesocial.posts.entity.Post;
import com.freesocial.posts.repository.PostRepository;
import com.freesocial.posts.service.PostService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = FreeSocialPostsApplication.class)
class PostServiceTests extends BasicTest {

    @MockBean
    private PostRepository postRepository;

    @Autowired
    private PostService postService;

    @Test
    void createPostWithoutTextOrFile() {
        PostDTO postDto = new PostDTO();

        Post testPost = Post.create(postDto, "uuid");

        //Will return the same object on save
        when(postRepository.save(Mockito.any(Post.class))).thenReturn(testPost);

        assertThrows(IllegalArgumentException.class, () -> {
            postService.create(testPost, null);
        }, "Text or file must be provided");

    }

    @Test
    void createPost() {
        PostDTO postDto = new PostDTO();
        postDto.setText("New post");

        Post testPost = Post.create(postDto, "uuid");

        //Will return the same object on save
        when(postRepository.save(Mockito.any(Post.class))).thenReturn(testPost);

        testPost = postService.create(testPost, null);

        assertNotNull(testPost, "Post must exists");

        assertNull(testPost.getId(), "ID must not exists (database generated)");

        assertNotNull(testPost.getPostUuid(), "Post UUID must exists");

        assertNotNull(testPost.getUserUuid(), "User UUID must exists");

        assertEquals(postDto.getText(), testPost.getContent().getText(),
                "Text must be equals in DTO and in the saved post");
    }

}
