package com.freesocial.posts.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class PostLikeCounter {

    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    private Integer count;

    @NotNull
    @OneToOne
    @JoinColumn(updatable = false)
    @EqualsAndHashCode.Include
    private Post post;

    @Version
    private Integer version;

    @PrePersist
    private void prePersist() {
        count = 0;
    }

    PostLikeCounter(Post post) {
        this.post = post;
    }

    public void incrementOrDecrement(boolean like) {
        if (like) {
            count++;
        } else {
            count--;
        }
    }

}
