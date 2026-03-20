package com.redteclab.cqrs2.command.application;

import com.redteclab.cqrs2.command.domain.OfferAggregate;
import com.redteclab.cqrs2.command.infrastructure.messaging.EventPublisher;
import com.redteclab.cqrs2.shared.event.DomainEvent;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OfferCommandHandler {

    private final EventStore eventStore;
    private final EventPublisher eventPublisher;

    public OfferCommandHandler(EventStore eventStore, EventPublisher eventPublisher) {
        this.eventStore = eventStore;
        this.eventPublisher = eventPublisher;
    }

    public void handle(CreateOfferCommand command) {
        var event = OfferAggregate.create(command.title(), command.price());
        eventStore.append(event.aggregateId(), 0, event);
        eventPublisher.publish(event);
    }

    public void handle(UpdateOfferCommand command) {
        List<DomainEvent> events = eventStore.loadEvents(command.offerId());
        if (events.isEmpty()) {
            throw new IllegalArgumentException("Offer not found: " + command.offerId());
        }
        var aggregate = OfferAggregate.rehydrate(events);
        var event = aggregate.update(command.title(), command.price());
        eventStore.append(command.offerId(), aggregate.version(), event);
        eventPublisher.publish(event);
    }

    public void handle(DeactivateOfferCommand command) {
        List<DomainEvent> events = eventStore.loadEvents(command.offerId());
        if (events.isEmpty()) {
            throw new IllegalArgumentException("Offer not found: " + command.offerId());
        }
        var aggregate = OfferAggregate.rehydrate(events);
        var event = aggregate.deactivate();
        eventStore.append(command.offerId(), aggregate.version(), event);
        eventPublisher.publish(event);
    }
}
