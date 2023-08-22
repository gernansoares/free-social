package com.freesocial.lib.config.kafka;

import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaAdmin;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaTopicConfig {

    public static final String TOKEN_REGISTRATION_TOPIC = "token-registration";
    public static final String TOKEN_REMOVAL_TOPIC = "token-removal";
    public static final String DELETE_ALL_TOKENS_TOPIC = "delete-all-tokens";

    public static final String BOOTSTRAP_ADRESS = "localhost:9092";

    @Bean
    public KafkaAdmin kafkaAdmin() {
        Map<String, Object> configs = new HashMap<>();
        configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_ADRESS);
        return new KafkaAdmin(configs);
    }

    @Bean
    public NewTopic tokenRegistration() {
        return new NewTopic(TOKEN_REGISTRATION_TOPIC, 1, (short) 1);
    }

    @Bean
    public NewTopic tokenRemoval() {
        return new NewTopic(TOKEN_REMOVAL_TOPIC, 1, (short) 1);
    }

}
