package com.freesocial.security.config;

import com.freesocial.lib.config.kafka.KafkaConsumerConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;

/**
 * Defines configuration for listening to topics
 * All listened topics must have its NewTopic and ListenerContainerFactory methods
 */
@Configuration
public class KafkaConfig {

    private static final String DELETE_ALL_TOKENS_TOPIC = "delete-all-tokens";

    @Autowired
    private KafkaConsumerConfig kafkaConsumerConfig;

    @Bean
    public NewTopic deleteAllTokens() {
        return new NewTopic(DELETE_ALL_TOKENS_TOPIC, 1, (short) 1);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, String> deleteAllTokensKafkaListenerContainerFactory() {
        return kafkaConsumerConfig.kafkaListenerContainerFactory();
    }

}
