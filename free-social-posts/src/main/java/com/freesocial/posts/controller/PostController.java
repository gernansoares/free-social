package com.freesocial.posts.controller;

import com.freesocial.lib.config.GlobalContants;
import com.freesocial.posts.dto.PostDTO;
import com.freesocial.posts.entity.Post;
import com.freesocial.posts.service.PostContentService;
import com.freesocial.posts.service.PostLikeService;
import com.freesocial.posts.service.PostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/posts")
@Slf4j
public class PostController {

    @Autowired
    private PostService postService;

    @Autowired
    private PostLikeService postLikesService;

    @Autowired
    private PostContentService postContentService;

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    @Operation(summary = "Creates a new post")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Post created"),
            @ApiResponse(responseCode = "400", description = "Text or file missing | Invalid file extension"),
            @ApiResponse(responseCode = "401", description = "User unauthorized"),
    })
    public Mono<String> create(@RequestHeader(GlobalContants.HEADER_UUID) String userUuid,
                               @RequestPart @Valid PostDTO newPost,
                               @RequestPart(required = false) FilePart upload) {
        Post post = Post.create(newPost, userUuid);
        log.info(String.format("User with UUID %s adding post with UUID %s", post.getUserUuid(), post.getPostUuid()));
        postService.create(post, upload);
        log.info(String.format("User with UUID %s added post with UUID %s successfully", post.getUserUuid(), post.getPostUuid()));
        return Mono.just(post.getPostUuid());
    }

    @PatchMapping("{postUuid}")
    @ResponseStatus(code = HttpStatus.OK)
    @Operation(summary = "Creates likes or remove like of a post")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Like created"),
            @ApiResponse(responseCode = "400", description = "Post not found"),
            @ApiResponse(responseCode = "401", description = "User unauthorized"),
    })
    public void like(@RequestHeader(GlobalContants.HEADER_UUID) String userUuid,
                     @PathVariable String postUuid) {
        log.info(String.format("User with UUID %s likes post with UUID %s", userUuid, postUuid));
        postLikesService.like(postUuid, userUuid);
        log.info(String.format("User with UUID %s liked post with UUID %s successfully", userUuid, postUuid));
    }

    @PutMapping("{postUuid}")
    @ResponseStatus(code = HttpStatus.OK)
    @Operation(summary = "Update a post's")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Post updated"),
            @ApiResponse(responseCode = "400", description = "Post not found/don't belong to user | Invalid post's text"),
            @ApiResponse(responseCode = "401", description = "User unauthorized"),
    })
    public void update(@RequestHeader(GlobalContants.HEADER_UUID) String userUuid,
                       @PathVariable String postUuid,
                       @RequestBody @Valid PostDTO postDto) {
        log.info(String.format("Updating post with UUID %s by user with UUID %s", postUuid, userUuid));
        postContentService.update(postDto, postUuid, userUuid);
        log.info(String.format("Post with UUID %s has updated successfully by user with UUID %s", postUuid, userUuid));
    }

    @DeleteMapping("{postUuid}")
    @ResponseStatus(code = HttpStatus.OK)
    @Operation(summary = "Delete user and its dependencies")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Post deleted"),
            @ApiResponse(responseCode = "400", description = "Post not found/don't belong to user"),
            @ApiResponse(responseCode = "401", description = "User unauthorized"),
    })
    public void delete(@RequestHeader(GlobalContants.HEADER_UUID) String userUuid,
                       @PathVariable String postUuid) {
        log.info(String.format("Deleting post with UUID %s by user with UUID %s", postUuid, userUuid));
        postService.delete(postUuid, userUuid);
        log.info(String.format("Post with UUID %s has deleted successfully by user with UUID %s", postUuid, userUuid));
    }

}
