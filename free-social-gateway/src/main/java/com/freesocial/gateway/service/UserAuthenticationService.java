package com.freesocial.gateway.service;

import com.freesocial.gateway.common.util.Constants;
import com.freesocial.gateway.entity.UserAuthentication;
import com.freesocial.gateway.repository.UserAuthenticationRepository;
import com.freesocial.lib.config.exceptions.DisabledException;
import com.freesocial.lib.config.exceptions.UserNotFoundException;
import com.freesocial.lib.config.security.AvailableRoles;
import com.freesocial.lib.config.security.UserInfo;
import com.freesocial.lib.properties.ErroUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.Optional;

@Service
public class UserAuthenticationService {

    @Autowired
    private UserAuthenticationRepository userAuthenticationRepository;

    /**
     * Loads user from database based on its username
     *
     * @param username user's username
     * @return user's information including username, password, enabled and roles
     * @throws UsernameNotFoundException if user is not found
     * @throws DisabledException         if user is not enabled
     */
    public Mono<UserInfo> findByUsername(String username) throws UsernameNotFoundException, DisabledException {
        Optional<UserAuthentication> userOpt = userAuthenticationRepository.findByUsernameIgnoreCase(username);

        if (!userOpt.isPresent()) {
            return Mono.empty();
        }

        UserAuthentication user = userOpt.get();

        if (!user.getUser().isEnabled()) {
            return Mono.empty();
        }

        return Mono.just(new UserInfo(user.getUsername(), user.getPassword(), user.getUser().getUuid(),
                user.getUser().isEnabled(), Arrays.asList(AvailableRoles.values())));
    }

}