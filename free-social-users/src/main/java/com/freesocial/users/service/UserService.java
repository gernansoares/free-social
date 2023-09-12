package com.freesocial.users.service;

import com.freesocial.lib.properties.ErrorUtil;
import com.freesocial.users.common.util.Constants;
import com.freesocial.users.entity.FreeSocialUser;
import com.freesocial.users.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(rollbackFor = Exception.class)
public class UserService {

    private final String DELETE_ALL_TOKENS_TOPIC = "delete-all-tokens";

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    private UserAuthenticationService userAuthenticationService;

    @Autowired
    private UserRepository userRepository;

    /**
     * Creates a new user
     *
     * @param user to be saved
     * @return the recently added user with its own ID and UUID
     */
    public FreeSocialUser create(FreeSocialUser user) {
        userAuthenticationService.validateNewUser(user.getAuthentication());
        return userRepository.save(user);
    }

    /**
     * Deletes a user using its UUID
     * Sends a message (by Kafka) to security service to remove all deleted user's tokens
     *
     * @param user to be deleted UUID
     */
    public void delete(String uuid) {
        Optional<FreeSocialUser> user = userRepository.findByUuid(uuid);
        userRepository.delete(user.orElseThrow(() -> new IllegalArgumentException(ErrorUtil.getMessage(Constants.USER_NOT_FOUND))));
        kafkaTemplate.send(DELETE_ALL_TOKENS_TOPIC, uuid);
    }

    public List<FreeSocialUser> findAll() {
        return userRepository.findAll();
    }

}
