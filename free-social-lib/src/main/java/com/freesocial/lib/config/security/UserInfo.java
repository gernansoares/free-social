package com.freesocial.lib.config.security;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@ToString
@AllArgsConstructor
@Getter
public class UserInfo {

    private static final long serialVersionUID = 1L;

    private String username;

    private String password;

    private String uuid;

    private Boolean enabled;

    private List<AvailableRoles> roles;

    public String getUsername() {
        return username;
    }

    public boolean isAccountNonExpired() {
        return false;
    }

    public boolean isAccountNonLocked() {
        return false;
    }

    public boolean isCredentialsNonExpired() {
        return false;
    }

    public boolean isEnabled() {
        return this.enabled;
    }

    @JsonIgnore
    public String getPassword() {
        return password;
    }

}