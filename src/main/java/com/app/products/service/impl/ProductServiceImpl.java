package com.app.products.service.impl;

import com.app.products.rest.CreateProductRestModel;
import com.app.products.service.ProductCreateEvent;
import com.app.products.service.ProductService;
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
    public String createProduct(CreateProductRestModel product) throws ExecutionException, InterruptedException {
        String productId = UUID.randomUUID().toString();

        // TODO: Persist Product details into DB before publishing an Event.

        ProductCreateEvent productCreateEvent = new ProductCreateEvent(
                product.getTitle(),
                product.getPrice(),
                product.getQuantity(),
                productId
        );

        SendResult<String, ProductCreateEvent> result = kafkaTemplate.send("product-created-events-topic", productId, productCreateEvent).get();

        LOGGER.info("Partition: {}", result.getRecordMetadata().partition());
        LOGGER.info("Topic: {}", result.getRecordMetadata());

        return productId;
    }
}