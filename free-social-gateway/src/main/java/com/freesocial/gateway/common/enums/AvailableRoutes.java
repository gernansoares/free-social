package com.freesocial.gateway.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Path and URI will be automatically added to routes configuration on start up
 * null URI means that route should be ignored (means that the gateway should answer the request or no one will)
 * Path will be automatically added to security configuration on start up
 * public or private access will depend on privateRoute field
 */
@AllArgsConstructor
@Getter
public enum AvailableRoutes {

    LOGIN("/auth/**", "http://localhost:8082/auth/", false),
    LOGOUT("/logout/**", "http://localhost:8082/logout/", true),
    NEW_USER("/newuser/**", "http://localhost:8081/newuser/", false),
    USERS("/users/**", "http://localhost:8081/users/", true);

    private String path;
    private String uri;
    private boolean privateRoute;


}
