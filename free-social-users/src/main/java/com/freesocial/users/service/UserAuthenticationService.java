package com.freesocial.users.service;

import com.freesocial.lib.config.exceptions.UsernameAlreadyExistsException;
import com.freesocial.lib.config.exceptions.UserNotFoundException;
import com.freesocial.lib.properties.ErroUtil;
import com.freesocial.users.common.util.Constants;
import com.freesocial.users.common.util.UserUtils;
import com.freesocial.users.dto.UserAuthenticationDTO;
import com.freesocial.users.entity.UserAuthentication;
import com.freesocial.users.repository.UserAuthenticationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional(rollbackFor = Exception.class)
public class UserAuthenticationService {

    @Autowired
    private UserAuthenticationRepository userAuthenticationRepository;

    @Autowired
    private BCryptPasswordEncoder encoder;

    /**
     * Check if passwords match
     *
     * @param hashedPassword  encoded password
     * @param passwordConfirm not encoded password confirmation
     * @throws IllegalArgumentException if passwords does not match
     */
    private void checkPasswordAndConfirmationMatch(String hashedPassword, String passwordConfirm) {
        if (!BCrypt.checkpw(passwordConfirm, hashedPassword)) {
            throw new IllegalArgumentException(ErroUtil.getMessage(Constants.PASSWORD_CONFIRMATION_NOT_MATCH));
        }
    }

    /**
     * Validates if user password and confirmation checks, also validates if its username already exists
     *
     * @param userAuthentication information to be validated, password must be hashed, while confirmation must be raw
     * @throws UsernameAlreadyExistsException if username exists already
     */
    public void validateNewUser(UserAuthentication userAuthentication) {
        checkPasswordAndConfirmationMatch(userAuthentication.getPassword(), userAuthentication.getPasswordConfirm());

        userAuthenticationRepository.findByUsernameIgnoreCase(userAuthentication.getUsername())
                .ifPresent((p) -> {
                    throw new UsernameAlreadyExistsException(ErroUtil.getMessage(Constants.USERNAME_ALREADY_IN_USE));
                });
    }

    /**
     * Check for user existence and username usage
     *
     * @param username new username
     * @param userUuid identifies the user
     * @throws UserNotFoundException          if user is not found
     * @throws UsernameAlreadyExistsException if username already exists and does not belong
     *                                        to the same user which is updating
     */
    private void checkForValidUserAndUsername(String username, String userUuid) {
        Optional<UserAuthentication> optUserAuthentication = userAuthenticationRepository.findByUser_Uuid(userUuid);
        UserAuthentication userAuthentication = optUserAuthentication
                .orElseThrow(() -> new UserNotFoundException(ErroUtil.getMessage(Constants.USER_NOT_FOUND)));

        Optional<UserAuthentication> otherUserAuthentication = userAuthenticationRepository.findByUsernameIgnoreCase(username);
        otherUserAuthentication.ifPresent((other) -> {
            throw new UsernameAlreadyExistsException(ErroUtil.getMessage(Constants.USERNAME_ALREADY_IN_USE));
        });
    }

    /**
     * Update user's authentication info (aka username/password)
     *
     * @param authentication new authentication information
     * @param userUuid       identifies the user
     */
    public void update(UserAuthenticationDTO authentication, String userUuid) {
        //Prepares username and password according to rules
        String username = UserUtils.prepareUsername(authentication.getUsername());
        String password = UserUtils.encodePassword(authentication.getPassword());

        checkPasswordAndConfirmationMatch(password, authentication.getPasswordConfirm());
        checkForValidUserAndUsername(username, userUuid);

        userAuthenticationRepository.updateUsernameAndPasswordByUserUuid(username, password, userUuid);
    }

}
