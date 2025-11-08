package com.app.products.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConfig {

    @Bean
    NewTopic createTopic () {
        Map<String, String> configsMap = new HashMap<>();
        configsMap.put("cleanup.policy", "compact");
        configsMap.put("retention.ms", "604800000");

        return TopicBuilder
                .name("product-created-events-topic")
                .partitions(3)
                .replicas(1)
                .configs(configsMap)
                .build();
    }

}
