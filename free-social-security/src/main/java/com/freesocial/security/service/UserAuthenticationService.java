package com.freesocial.security.service;

import com.freesocial.lib.config.exceptions.DisabledException;
import com.freesocial.lib.config.security.AvailableRoles;
import com.freesocial.lib.config.security.jwt.UserInfo;
import com.freesocial.security.repository.UserAuthenticationRepository;
import com.freesocial.security.dto.AuthRequest;
import com.freesocial.security.entity.UserAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.Optional;

@Service
@Transactional(rollbackFor = Exception.class)
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