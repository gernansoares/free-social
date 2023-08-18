package com.freesocial.users.entity;

import com.freesocial.users.common.util.UserUtils;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserAuthentication {

    @Id
    @GeneratedValue
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
        UserUtils userUtils = new UserUtils();
        this.username = userUtils.prepareUsername(username);
        this.password = userUtils.encodePassword(password);
        this.passwordConfirm = passwordConfirm;
        this.user = user;
    }
}
