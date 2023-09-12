package com.freesocial.posts.entity;

import com.freesocial.posts.dto.PostDTO;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Post {

    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    @Column(updatable = false)
    private String userUuid;

    @NotNull
    @Column(updatable = false, unique = true)
    @EqualsAndHashCode.Include
    private String postUuid;

    @NotNull
    private boolean enabled;

    @NotNull
    @OneToOne(mappedBy = "post", fetch = FetchType.LAZY,
            cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private PostContent content;

    @NotNull
    @OneToOne(mappedBy = "post", fetch = FetchType.LAZY,
            cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private PostLikeCounter likes;

    public static Post create(PostDTO dto, String userUuid) {
        Post post = new Post();
        PostContent postContent = new PostContent(dto.getText(), post);
        PostLikeCounter postLikes = new PostLikeCounter(post);

        post.userUuid = userUuid;
        post.postUuid = UUID.randomUUID().toString();
        post.content = postContent;
        post.likes = postLikes;
        post.enabled = true;
        return post;
    }


}
