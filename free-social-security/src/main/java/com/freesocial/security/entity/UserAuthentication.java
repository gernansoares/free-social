package com.freesocial.security.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class UserAuthentication {

    @Id
    @GeneratedValue
    @EqualsAndHashCode.Include
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
