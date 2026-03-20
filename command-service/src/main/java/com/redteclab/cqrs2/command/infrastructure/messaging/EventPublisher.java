package com.redteclab.cqrs2.command.infrastructure.messaging;

import com.redteclab.cqrs2.shared.event.DomainEvent;
import com.redteclab.cqrs2.shared.event.OfferEventEnvelope;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class EventPublisher {

    private static final Logger LOG = LoggerFactory.getLogger(EventPublisher.class);

    private final RabbitTemplate rabbitTemplate;

    public EventPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void publish(DomainEvent event) {
        var envelope = OfferEventEnvelope.wrap(event);
        String routingKey = "offer." + event.getClass().getSimpleName();
        rabbitTemplate.convertAndSend(RabbitConfig.EVENTS_EXCHANGE, routingKey, envelope);
        LOG.info("Published {} for aggregate {}", envelope.eventType(), envelope.aggregateId());
    }
}
