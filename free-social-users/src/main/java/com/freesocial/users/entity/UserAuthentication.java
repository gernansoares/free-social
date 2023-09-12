package com.freesocial.users.entity;

import com.freesocial.users.common.util.UserUtils;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class UserAuthentication {

    @Id
    @GeneratedValue
    private Long id;

    @Setter
    @NotNull
    @Column(unique = true)
    private String username;

    @Setter
    @NotNull
    private String password;

    @NotNull
    @OneToOne
    @JoinColumn(updatable = false)
    @EqualsAndHashCode.Include
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
