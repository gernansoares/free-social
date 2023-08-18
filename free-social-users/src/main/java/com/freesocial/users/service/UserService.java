package com.freesocial.users.service;

import com.freesocial.lib.config.tests.BasicTest;
import com.freesocial.users.entity.FreeSocialUser;
import com.freesocial.users.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
     * Deleter a user
     *
     * @param user to be deleted
     */
    public void delete(FreeSocialUser user) {
        userRepository.delete(user);
    }

}
