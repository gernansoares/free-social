package com.freesocial.security.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class FreeSocialUser {

    @Id
    @GeneratedValue
    private Long id;

    @EqualsAndHashCode.Include
    private String uuid;

    private boolean enabled;

    @OneToOne(mappedBy = "user", fetch = FetchType.LAZY)
    private UserAuthentication authentication;

    public static FreeSocialUser of(String username, String password, boolean enabled) {
        FreeSocialUser user = new FreeSocialUser();
        user.authentication = new UserAuthentication(username, password, user);
        user.enabled = enabled;
        user.uuid = UUID.randomUUID().toString();
        return user;
    }

}
