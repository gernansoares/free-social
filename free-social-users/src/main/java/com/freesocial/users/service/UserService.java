package com.freesocial.users.service;

import com.freesocial.lib.config.tests.BasicTest;
import com.freesocial.lib.properties.ErroUtil;
import com.freesocial.users.common.util.Constants;
import com.freesocial.users.entity.FreeSocialUser;
import com.freesocial.users.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional(rollbackFor = Exception.class)
public class UserService extends BasicTest {

    @Autowired
    private UserAuthenticationService userAuthenticationService;

    @Autowired
    private UserRepository userRepository;

    /**
     * Creates a new user
     *
     * @param user to be saved
     * @return the recently add user with its own ID and UUID
     */
    public FreeSocialUser create(FreeSocialUser user) {
        userAuthenticationService.validateNewUser(user.getAuthentication());
        return userRepository.save(user);
    }

    /**
     * Deleter a user using its UUID
     *
     * @param user to be deleted UUID
     */
    public void delete(String uuid) {
        Optional<FreeSocialUser> user = userRepository.findByUuid(uuid);
        user.orElseThrow(() -> new IllegalArgumentException(ErroUtil.getMessage(Constants.USER_NOT_FOUND)));
        userRepository.delete(user.get());
    }

}
