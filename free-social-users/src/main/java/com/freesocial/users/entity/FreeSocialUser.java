package com.freesocial.users.entity;

import com.freesocial.users.dto.UserSignUpDTO;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
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
    @EqualsAndHashCode.Include
    private Long id;

    @NotNull
    @Column(updatable = false, unique = true)
    private String uuid;

    @NotNull
    private boolean enabled;

    @NotNull
    @OneToOne(mappedBy = "user", fetch = FetchType.LAZY,
            cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private UserAuthentication authentication;

    @NotNull
    @OneToOne(mappedBy = "user", fetch = FetchType.LAZY,
            cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private UserProfile profile;

    public static FreeSocialUser of(UserSignUpDTO dto) {
        FreeSocialUser user = new FreeSocialUser();
        user.uuid = UUID.randomUUID().toString();
        user.enabled = true;
        user.authentication = new UserAuthentication(dto.getUsername(), dto.getPassword(), dto.getPasswordConfirm(), user);
        user.profile = new UserProfile(dto.getName(), dto.getBio(), user);
        return user;
    }


}
