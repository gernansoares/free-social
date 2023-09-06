package com.freesocial.users.service;

import com.freesocial.lib.config.exceptions.UsernameAlreadyExistsException;
import com.freesocial.lib.config.exceptions.UserNotFoundException;
import com.freesocial.lib.properties.ErrorUtil;
import com.freesocial.users.common.util.Constants;
import com.freesocial.users.common.util.UserUtils;
import com.freesocial.users.dto.UserAuthenticationDTO;
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
     * Check if passwords match
     *
     * @param hashedPassword  encoded password
     * @param passwordConfirm not encoded password confirmation
     * @throws IllegalArgumentException if passwords does not match
     */
    private void validatePasswordAndConfirmationMatch(String hashedPassword, String passwordConfirm) {
        if (!BCrypt.checkpw(passwordConfirm, hashedPassword)) {
            throw new IllegalArgumentException(ErrorUtil.getMessage(Constants.PASSWORD_CONFIRMATION_NOT_MATCH));
        }
    }

    /**
     * Validates if user password and confirmation checks, also validates if its username already exists
     *
     * @param userAuthentication information to be validated, password must be hashed, while confirmation must be raw
     * @throws UsernameAlreadyExistsException if username exists already
     */
    public void validateNewUser(UserAuthentication userAuthentication) {
        validatePasswordAndConfirmationMatch(userAuthentication.getPassword(), userAuthentication.getPasswordConfirm());

        userAuthenticationRepository.findByUsernameIgnoreCase(userAuthentication.getUsername())
                .ifPresent((p) -> {
                    throw new UsernameAlreadyExistsException(ErrorUtil.getMessage(Constants.USERNAME_ALREADY_IN_USE));
                });
    }

    /**
     * Validate if a username change is valid by checking if actual username is not in use
     * or belongs to the user in which is updating
     *
     * @param username new username
     * @param userUuid identifies the user
     * @throws UserNotFoundException          if user is not found
     * @throws UsernameAlreadyExistsException if username already exists and does not belong
     *                                        to the same user which is updating
     */
    private void validateUsernameChange(String oldUsername, String newUserName) {
        Optional<UserAuthentication> otherUserAuthentication = userAuthenticationRepository.findByUsernameIgnoreCase(newUserName);
        otherUserAuthentication.filter(other -> !other.getUsername().equals(oldUsername)).ifPresent((other) -> {
            throw new UsernameAlreadyExistsException(ErrorUtil.getMessage(Constants.USERNAME_ALREADY_IN_USE));
        });
    }

    /**
     * Update user's authentication info (aka username/password)
     *
     * @param authenticationDto new authentication information
     * @param userUuid          identifies the user
     */
    public void update(UserAuthenticationDTO authenticationDto, String userUuid) {
        String username = UserUtils.prepareUsername(authenticationDto.getUsername());
        String password = UserUtils.encodePassword(authenticationDto.getPassword());

        Optional<UserAuthentication> optUserAuthentication = userAuthenticationRepository.findByUser_Uuid(userUuid);
        UserAuthentication userAuthentication = optUserAuthentication
                .orElseThrow(() -> new UserNotFoundException(ErrorUtil.getMessage(Constants.USER_NOT_FOUND)));

        validatePasswordAndConfirmationMatch(password, authenticationDto.getPasswordConfirm());
        validateUsernameChange(userAuthentication.getUsername(), username);

        userAuthentication.setUsername(username);
        userAuthentication.setPassword(password);
        userAuthenticationRepository.save(userAuthentication);
    }

}
