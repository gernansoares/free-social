package com.freesocial.posts.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class UserPostLike {

    @Id
    @GeneratedValue
    @EqualsAndHashCode.Include
    private Long id;

    @NotBlank
    private String userUuid;

    @NotNull
    @ManyToOne
    @JoinColumn(updatable = false)
    private Post post;

    public static UserPostLike create(Post post, String userUuid) {
        UserPostLike postLike = new UserPostLike();
        postLike.userUuid = userUuid;
        postLike.post = post;
        return postLike;
    }

}
