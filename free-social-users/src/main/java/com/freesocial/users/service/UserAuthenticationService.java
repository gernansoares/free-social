package com.freesocial.users.service;

import com.freesocial.lib.properties.ErroUtil;
import com.freesocial.users.common.util.Constants;
import com.freesocial.users.entity.UserAuthentication;
import com.freesocial.users.repository.UserAuthenticationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional(rollbackFor = Exception.class)
public class UserAuthenticationService {

    @Autowired
    private UserAuthenticationRepository userAuthenticationRepository;

    /**
     * Validates if user password and confirmation checks, also validates if its username already exists
     *
     * @param userAuthentication information to be validated, password must be hashed, while confirmation must be raw
     */
    public void validateNewUser(UserAuthentication userAuthentication) {
        if (!BCrypt.checkpw(userAuthentication.getPasswordConfirm(), userAuthentication.getPassword())) {
            throw new IllegalArgumentException(ErroUtil.getMessage(Constants.PASSWORD_CONFIRMATION_NOT_MATCH));
        }
        Optional<UserAuthentication> userAuthenticationOpt =
                userAuthenticationRepository.getByUsernameIgnoreCase(userAuthentication.getUsername());
        userAuthenticationOpt.ifPresent(s -> {
            throw new IllegalArgumentException(ErroUtil.getMessage(Constants.USERNAME_ALREADY_IN_USE));
        });
    }

}
