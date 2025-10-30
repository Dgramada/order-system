package com.dgramada.order;

import com.dgramada.schemas.events.OrderCreatedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class OrderConsumerService {

    private final Logger logger = LoggerFactory.getLogger(OrderConsumerService.class);

    @KafkaListener(topics = "orders", groupId = "${spring.kafka.consumer.group-id}")
    public void listen(OrderCreatedEvent event) {
        logger.info("Received order created event with eventId: {}", event.getEventId());
    }
}
