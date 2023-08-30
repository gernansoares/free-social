package com.freesocial.security.config;

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
    public NewTopic deleteAllTokens() {
        return new NewTopic(KafkaTopicConfig.DELETE_ALL_TOKENS_TOPIC, 1, (short) 1);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, String> deleteAllTokensKafkaListenerContainerFactory() {
        return kafkaConsumerConfig.kafkaListenerContainerFactory(KafkaTopicConfig.DELETE_ALL_TOKENS_TOPIC);
    }

}
