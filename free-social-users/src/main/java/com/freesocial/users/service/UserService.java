package com.freesocial.users.service;

import com.freesocial.lib.config.kafka.KafkaTopicConfig;
import com.freesocial.lib.config.tests.BasicTest;
import com.freesocial.lib.properties.ErroUtil;
import com.freesocial.users.common.util.Constants;
import com.freesocial.users.entity.FreeSocialUser;
import com.freesocial.users.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.util.Optional;

@Service
@Transactional(rollbackFor = Exception.class)
public class UserService {

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
     * @return the recently add user with its own ID and UUID
     */
    public Mono<FreeSocialUser> create(FreeSocialUser user) {
        userAuthenticationService.validateNewUser(user.getAuthentication());
        return Mono.just(userRepository.save(user));
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
        kafkaTemplate.send(KafkaTopicConfig.DELETE_ALL_TOKENS_TOPIC, uuid);
    }

}
