package com.freesocial.posts.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;

@Entity
@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class PostContent {

    @Id
    @GeneratedValue
    @EqualsAndHashCode.Include
    private Long id;

    @Column(columnDefinition = "TEXT")
    private String text;

    @NotNull
    @OneToOne
    @JoinColumn(updatable = false)
    private Post post;

}
