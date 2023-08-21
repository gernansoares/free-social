package com.freesocial.gateway.service;

import com.freesocial.gateway.dto.AuthRequest;
import com.freesocial.gateway.entity.UserAuthentication;
import com.freesocial.gateway.repository.UserAuthenticationRepository;
import com.freesocial.lib.config.exceptions.DisabledException;
import com.freesocial.lib.config.security.AvailableRoles;
import com.freesocial.lib.config.security.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.Optional;

@Service
public class UserAuthenticationService {

    @Autowired
    private UserAuthenticationRepository userAuthenticationRepository;

    /**
     * Loads user from database based on its username, checks for password match and user status
     *
     * @param request user's username and password
     * @return user's information including username, password, enabled and roles
     */
    public Mono<UserInfo> login(AuthRequest request) throws UsernameNotFoundException, DisabledException {
        Optional<UserAuthentication> userOpt = userAuthenticationRepository.findByUsernameIgnoreCase(request.getUsername());

        if (!userOpt.isPresent()) {
            return Mono.empty();
        }

        UserAuthentication user = userOpt.get();

        if (!user.getUser().isEnabled()
                || !BCrypt.checkpw(request.getPassword(), user.getPassword())) {
            return Mono.empty();
        }

        return Mono.just(new UserInfo(user.getUsername(), user.getPassword(), user.getUser().getUuid(),
                user.getUser().isEnabled(), Arrays.asList(AvailableRoles.values())));
    }

}