package com.redteclab.cqrs2.query.infrastructure.messaging;

import com.redteclab.cqrs2.query.infrastructure.projection.OfferProjector;
import com.redteclab.cqrs2.shared.event.OfferEventEnvelope;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class OfferEventConsumer {

    private static final Logger LOG = LoggerFactory.getLogger(OfferEventConsumer.class);

    private final OfferProjector projector;

    public OfferEventConsumer(OfferProjector projector) {
        this.projector = projector;
    }

    @RabbitListener(queues = RabbitConfig.EVENTS_QUEUE)
    public void onEvent(OfferEventEnvelope envelope) {
        LOG.info("Received event: {} for aggregate {}", envelope.eventType(), envelope.aggregateId());

        switch (envelope.payload()) {
            case com.redteclab.cqrs2.shared.event.OfferCreatedEvent e -> projector.on(e);
            case com.redteclab.cqrs2.shared.event.OfferUpdatedEvent e -> projector.on(e);
            case com.redteclab.cqrs2.shared.event.OfferDeactivatedEvent e -> projector.on(e);
            default -> LOG.warn("Unknown event type: {}", envelope.eventType());
        }
    }
}
