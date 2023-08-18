package com.freesocial.gateway.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FreeSocialUser {

    @Id
    private Long id;

    private String uuid;

    private boolean enabled;

    @OneToOne(mappedBy = "user", fetch = FetchType.LAZY)
    private UserAuthentication authentication;

}
