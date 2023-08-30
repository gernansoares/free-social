package com.freesocial.security.entity;

import com.freesocial.lib.config.security.JwtUtil;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@RequiredArgsConstructor(access = AccessLevel.PUBLIC)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class UserToken {

    @Id
    @GeneratedValue
    private Long id;

    @NonNull
    @NotNull
    @EqualsAndHashCode.Include
    @Column(unique = true, columnDefinition = "TEXT")
    private String token;

    @NonNull
    @NotNull
    private String userUuid;

}
