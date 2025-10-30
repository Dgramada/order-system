package com.dgramada.kafka;

import com.dgramada.schemas.events.OrderCreatedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaProducerService {

    private final Logger logger = LoggerFactory.getLogger(KafkaProducerService.class);

    private final KafkaTemplate<String, OrderCreatedEvent> kafkaTemplate;

    public KafkaProducerService(KafkaTemplate<String, OrderCreatedEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void send(OrderCreatedEvent event) {
        logger.info("Publishing order created event for orderId: {}", event.getOrderId());
        kafkaTemplate.send("orders", String.valueOf(event.getOrderId()), event);
    }
}
