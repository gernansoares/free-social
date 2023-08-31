package com.freesocial.users.entity;

import com.freesocial.users.common.util.UserUtils;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class UserAuthentication {

    @Id
    @GeneratedValue
    @EqualsAndHashCode.Include
    private Long id;

    @NotNull
    @Column(unique = true)
    private String username;

    @NotNull
    private String password;

    @NotNull
    @OneToOne
    @JoinColumn(updatable = false)
    private FreeSocialUser user;

    @Transient
    private String passwordConfirm;

    UserAuthentication(String username, String password, String passwordConfirm, FreeSocialUser user) {
        this.username = UserUtils.prepareUsername(username);
        this.password = UserUtils.encodePassword(password);
        this.passwordConfirm = passwordConfirm;
        this.user = user;
    }
}
