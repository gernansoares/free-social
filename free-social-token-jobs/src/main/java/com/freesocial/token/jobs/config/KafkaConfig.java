package com.freesocial.token.jobs.config;

import com.freesocial.lib.config.kafka.KafkaConsumerConfig;
import com.freesocial.lib.config.kafka.KafkaTopicConfig;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.KafkaAdmin;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConfig {

    @Autowired
    private KafkaConsumerConfig kafkaConsumerConfig;

    @Bean
    public NewTopic tokenRegistration() {
        return new NewTopic(KafkaTopicConfig.TOKEN_REGISTRATION_TOPIC, 1, (short) 1);
    }

    @Bean
    public NewTopic tokenRemoval() {
        return new NewTopic(KafkaTopicConfig.TOKEN_REMOVAL_TOPIC, 1, (short) 1);
    }

    @Bean
    public NewTopic deleteAllTokens() {
        return new NewTopic(KafkaTopicConfig.DELETE_ALL_TOKENS_TOPIC, 1, (short) 1);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, String> tokenRegistrationKafkaListenerContainerFactory() {
        return kafkaConsumerConfig.kafkaListenerContainerFactory(KafkaTopicConfig.TOKEN_REGISTRATION_TOPIC);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, String> tokenRemovalKafkaListenerContainerFactory() {
        return kafkaConsumerConfig.kafkaListenerContainerFactory(KafkaTopicConfig.TOKEN_REMOVAL_TOPIC);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, String> deleteAllTokensKafkaListenerContainerFactory() {
        return kafkaConsumerConfig.kafkaListenerContainerFactory(KafkaTopicConfig.DELETE_ALL_TOKENS_TOPIC);
    }

}
