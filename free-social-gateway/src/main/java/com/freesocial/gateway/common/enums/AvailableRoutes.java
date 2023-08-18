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

    ACTUATOR("/actuator/**", null, false),
    SESSION("/session/**", null, true),
    NEW_LOGIN("/auth/**", null, false),
    NEW_USER("/newuser/**", "http://localhost:8081/newuser/", false),
    USERS("/users/**", "http://localhost:8081/users/", true);

    private String path;
    private String uri;
    private boolean privateRoute;


}
