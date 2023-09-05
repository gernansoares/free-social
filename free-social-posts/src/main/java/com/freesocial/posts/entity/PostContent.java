package com.freesocial.posts.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.codec.multipart.FilePart;

import java.util.Objects;
import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class PostContent {

    @Id
    @GeneratedValue
    @EqualsAndHashCode.Include
    private Long id;

    @Setter
    @Column(columnDefinition = "TEXT")
    private String text;

    @Setter
    @Column(columnDefinition = "TEXT", updatable = false, unique = true)
    private String fileDir;

    @NotNull
    @OneToOne
    @JoinColumn(updatable = false)
    private Post post;

    PostContent(String text, Post post) {
        this.text = text;
        this.post = post;
    }

}
