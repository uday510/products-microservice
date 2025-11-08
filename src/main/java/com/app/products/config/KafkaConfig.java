package com.app.products.config;

import com.app.core.ProductCreateEvent;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConfig {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Value("${spring.kafka.producer.key-serializer}")
    private String keySerializer;

    @Value("${spring.kafka.producer.value-serializer}")
    private String valueSerializer;

    @Value("${spring.kafka.producer.acks}")
    private String acks;

    @Value("${spring.kafka.producer.properties.delivery.timeout.ms}")
    private Integer deliveryTimeout;

    @Value("${spring.kafka.producer.linger-ms}")
    private Integer linger;

    @Value("${spring.kafka.producer.properties.request.timeout.ms}")
    private Integer requestTimeout;

    @Value("${spring.kafka.producer.properties.enable.idempotence}")
    private Boolean idempotence;

    @Value("${spring.kafka.producer.properties.max.in.flight.requests.per.connection}")
    private Integer inflightRequests;

    @Bean
    NewTopic createTopic() {
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

    @Bean
    public Map<String, Object> producerConfigs() {
        Map<String, Object> producerConfigMap = new HashMap<>();
        producerConfigMap.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        producerConfigMap.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, keySerializer);
        producerConfigMap.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, valueSerializer);
        producerConfigMap.put(ProducerConfig.ACKS_CONFIG, acks);
        producerConfigMap.put(ProducerConfig.DELIVERY_TIMEOUT_MS_CONFIG, deliveryTimeout);
        producerConfigMap.put(ProducerConfig.LINGER_MS_CONFIG, linger);
        producerConfigMap.put(ProducerConfig.REQUEST_TIMEOUT_MS_CONFIG, requestTimeout);
        producerConfigMap.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, idempotence);
        producerConfigMap.put(ProducerConfig.MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION, inflightRequests);
//        producerConfigMap.put(ProducerConfig.RETRIES_CONFIG, Integer.MAX_VALUE);

        return producerConfigMap;
    }

    @Bean
    public ProducerFactory<String, ProductCreateEvent> producerFactory() {
        return new DefaultKafkaProducerFactory<>(producerConfigs());
    }

    @Bean
    public KafkaTemplate<String, ProductCreateEvent> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }
}