package com.freesocial.users.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class UserProfile {

    @Id
    @GeneratedValue
    private Long id;

    @Setter
    @NotNull
    private String name;

    @Setter
    @Column(columnDefinition = "TEXT")
    private String bio;

    @NotNull
    @OneToOne
    @JoinColumn(updatable = false)
    @EqualsAndHashCode.Include
    private FreeSocialUser user;

    UserProfile(String name, String bio, FreeSocialUser user) {
        this.name = name;
        this.bio = bio;
        this.user = user;
    }
}
