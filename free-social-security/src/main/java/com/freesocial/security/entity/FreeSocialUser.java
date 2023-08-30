package com.freesocial.security.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FreeSocialUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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
