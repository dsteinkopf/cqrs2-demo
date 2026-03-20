package com.redteclab.cqrs2.shared.event;

import java.time.Instant;

public record OfferDeactivatedEvent(
        String aggregateId,
        int version,
        Instant occurredAt
) implements DomainEvent {
}
