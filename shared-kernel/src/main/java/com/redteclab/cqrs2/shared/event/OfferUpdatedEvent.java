package com.redteclab.cqrs2.shared.event;

import java.math.BigDecimal;
import java.time.Instant;

public record OfferUpdatedEvent(
        String aggregateId,
        int version,
        Instant occurredAt,
        String title,
        BigDecimal price
) implements DomainEvent {
}
