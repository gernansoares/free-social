package com.freesocial.posts.controller;

import com.freesocial.lib.config.security.JwtAuthenticationFilter;
import com.freesocial.posts.dto.PostDTO;
import com.freesocial.posts.entity.Post;
import com.freesocial.posts.entity.PostContent;
import com.freesocial.posts.service.PostContentService;
import com.freesocial.posts.service.PostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Optional;

@RestController
@RequestMapping("/posts")
@Slf4j
public class PostController {

    @Autowired
    private PostService postService;

    @Autowired
    private PostContentService postContentService;

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    @Operation(summary = "Creates a new post, UUID identifies the user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Post created"),
            @ApiResponse(responseCode = "400", description = "Invalid information"),
    })
    public Mono<String> create(@RequestHeader(JwtAuthenticationFilter.HEADER_UUID) String userUuid,
                               @RequestPart @Valid PostDTO newPost,
                               @RequestPart(required = false) FilePart upload) {
        Post post = Post.of(newPost, userUuid);
        log.info(String.format("User with UUID %s adding post with UUID %s", post.getUserUuid(), post.getPostUuid()));
        postService.create(post, upload);
        log.info(String.format("User with UUID %s added post with UUID %s successfully", post.getUserUuid(), post.getPostUuid()));
        return Mono.just(post.getPostUuid());
    }

    @PutMapping("{postUuid}")
    @ResponseStatus(code = HttpStatus.OK)
    @Operation(summary = "Update a post's")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Profile updated"),
            @ApiResponse(responseCode = "400", description = "Invalid information"),
    })
    public void updateProfile(@RequestHeader(JwtAuthenticationFilter.HEADER_UUID) String userUuid,
                              @PathVariable String postUuid,
                              @RequestBody @Valid PostDTO profileDto) {
        log.info(String.format("Updating post with UUID %s by user with UUID %s", postUuid, userUuid));
        postContentService.update(profileDto, postUuid, userUuid);
        log.info(String.format("Post with UUID %s has updated successfully by user with UUID %s", postUuid, userUuid));
    }

    @DeleteMapping("{postUuid}")
    @ResponseStatus(code = HttpStatus.OK)
    @Operation(summary = "Delete user and its dependencies, UUID identifies the user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User deleted"),
            @ApiResponse(responseCode = "400", description = "Invalid information"),
    })
    public void delete(@RequestHeader(JwtAuthenticationFilter.HEADER_UUID) String userUuid,
                       @PathVariable String postUuid) {
        log.info(String.format("Deleting post with UUID %s by user with UUID %s", postUuid, userUuid));
        postService.delete(postUuid, userUuid);
        log.info(String.format("Post with UUID %s has deleted successfully by user with UUID %s", postUuid, userUuid));
    }

}