package com.freesocial.security.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserAuthentication {

    @Id
    private Long id;

    private String username;

    private String password;

    @OneToOne
    private FreeSocialUser user;

    UserAuthentication(String username, String password, FreeSocialUser user) {
        this.username = username;
        this.password = password;
        this.user = user;
    }
}