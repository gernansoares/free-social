package com.freesocial.post.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.freesocial.lib.config.GlobalContants;
import com.freesocial.lib.config.tests.BasicTest;
import com.freesocial.lib.common.util.Profiles;
import com.freesocial.posts.FreeSocialPostsApplication;
import com.freesocial.posts.dto.PostDTO;
import com.freesocial.posts.entity.Post;
import com.freesocial.posts.repository.PostContentRepository;
import com.freesocial.posts.repository.PostRepository;
import com.freesocial.posts.service.PostService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = FreeSocialPostsApplication.class)
@AutoConfigureWebTestClient
@ActiveProfiles(profiles = Profiles.TESTS_NO_SECURITY)
class PostControllerIntegrationTests extends BasicTest {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private PostContentRepository postContentRepository;

    @Autowired
    private PostService postService;

    @Autowired
    private WebTestClient webTestClient;

    private PostDTO postToBeAdd;

    private Post addedPost;

    @BeforeEach
    public void createOnePost() {
        postToBeAdd = new PostDTO();
        postToBeAdd.setText("Today I'm feeling happy");

        addedPost = postService.create(Post.create(postToBeAdd, "uuid"), null);
    }

    @AfterEach
    public void deletePosts() {
        postRepository.deleteAll();
        postContentRepository.deleteAll();
    }


    @Test
    void createPostWithoutFile() throws Exception {
        MultipartBodyBuilder multipartBodyBuilder = new MultipartBodyBuilder();
        multipartBodyBuilder.part("newPost", new ObjectMapper().writeValueAsString(postToBeAdd))
                .contentType(MediaType.APPLICATION_JSON);

        webTestClient.post().uri("/posts")
                .header(GlobalContants.HEADER_UUID, "uuid")
                .body(BodyInserters.fromMultipartData(multipartBodyBuilder.build()))
                .exchange()
                .expectStatus().isCreated();

        assertEquals(2, postRepository.count(), "Must be 2, the first post is created automatically");

        assertEquals(postRepository.findAll().get(0).getContent().getText(), postToBeAdd.getText(),
                "DTO text and new post in database text must be equals");
    }

    @Test
    void createPostWithFile() throws Exception {
        Resource resource = new ClassPathResource("data/picture.png");

        MultipartBodyBuilder multipartBodyBuilder = new MultipartBodyBuilder();
        multipartBodyBuilder.part("newPost", new ObjectMapper().writeValueAsString(postToBeAdd))
                .contentType(MediaType.APPLICATION_JSON);
        multipartBodyBuilder.part("upload", resource)
                .contentType(MediaType.APPLICATION_OCTET_STREAM);

        webTestClient.post().uri("/posts")
                .header(GlobalContants.HEADER_UUID, "uuid")
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .body(BodyInserters.fromMultipartData(multipartBodyBuilder.build()))
                .exchange()
                .expectStatus().isCreated();

        assertTrue(resource.getFile().exists(), "File must exists in disk");

        assertEquals(2, postRepository.count(), "Must be 2, the first post is created automatically");

        assertEquals(postRepository.findAll().get(0).getContent().getText(), postToBeAdd.getText(),
                "DTO text and new post in database text must be equals");

        resource.getFile().delete();
    }

    @Test
    void updatePost() throws Exception {
        PostDTO postDto = new PostDTO();
        postDto.setText("Today I had the best day!");

        webTestClient.put().uri("/posts/{postUuid}", addedPost.getPostUuid())
                .header(GlobalContants.HEADER_UUID, addedPost.getUserUuid())
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new ObjectMapper().writeValueAsString(postDto))
                .exchange()
                .expectStatus().isOk();

        //Updates the post
        addedPost = postRepository.findByPostUuid(addedPost.getPostUuid()).get();

        assertNotEquals(postToBeAdd.getText(), addedPost.getContent().getText(), "Text must not be equals to the old DTO");

        assertEquals(addedPost.getContent().getText(), postDto.getText(), "Text must be equals to the new DTO");
    }

    @Test
    void deletePost() throws Exception {
        webTestClient.delete().uri("/posts/{postUUid}", addedPost.getPostUuid())
                .header(GlobalContants.HEADER_UUID, addedPost.getUserUuid())
                .exchange()
                .expectStatus().isOk();

        assertEquals(0, postRepository.count(), "Must be 0");
    }

}
