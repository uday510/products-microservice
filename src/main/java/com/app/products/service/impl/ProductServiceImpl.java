package com.app.products.service.impl;

import com.app.core.ProductCreateEvent;
import com.app.products.rest.CreateProductRestModel;
import com.app.products.service.ProductService;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.ExecutionException;

@Service
public class ProductServiceImpl implements ProductService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProductServiceImpl.class);

    private final KafkaTemplate<String, ProductCreateEvent> kafkaTemplate;

    public ProductServiceImpl(KafkaTemplate<String, ProductCreateEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public String createProduct(CreateProductRestModel product) {
        String productId = UUID.randomUUID().toString();

        ProductCreateEvent productCreateEvent = new ProductCreateEvent(
                product.getTitle(),
                product.getPrice(),
                product.getQuantity(),
                productId
        );

        ProducerRecord<String, ProductCreateEvent> record = new ProducerRecord<>(
                "product-created-events-topic",
                productId,
                productCreateEvent
        );

        record.headers().add("messageId", UUID.randomUUID().toString().getBytes());

        try {
            SendResult<String, ProductCreateEvent> result = kafkaTemplate
                    .send(record)
                    .get();

            LOGGER.info(
                    "Sent event for Product ID: {} to topic '{}' partition {} @ offset {}",
                    productId,
                    result.getRecordMetadata().topic(),
                    result.getRecordMetadata().partition(),
                    result.getRecordMetadata().offset()
            );

            return productId;
        } catch (InterruptedException | ExecutionException e) {
            Thread.currentThread().interrupt();
            LOGGER.error("Failed to send product creation event for {}: {}", productId, e.getMessage(), e);
            throw new RuntimeException("Failed to send Kafka message", e);
        }
    }
}