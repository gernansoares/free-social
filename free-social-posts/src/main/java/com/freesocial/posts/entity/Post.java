package com.freesocial.posts.entity;

import com.freesocial.posts.dto.PostDTO;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Post {

    @Id
    @GeneratedValue
    @EqualsAndHashCode.Include
    private Long id;

    @NotNull
    @Column(updatable = false)
    private String userUuid;

    @NotNull
    @Column(updatable = false, unique = true)
    private String postUuid;

    @NotNull
    private boolean enabled;

    @NotNull
    @OneToOne(mappedBy = "post", fetch = FetchType.LAZY,
            cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private PostContent content;

    public static Post create(PostDTO dto, String userUuid) {
        Post post = new Post();
        PostContent postContent = new PostContent(dto.getText(), post);
        post.userUuid = userUuid;
        post.postUuid = UUID.randomUUID().toString();
        post.content = postContent;
        post.enabled = true;
        return post;
    }


}
