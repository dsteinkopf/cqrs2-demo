package com.redteclab.cqrs2.shared.event;

import java.time.Instant;

public record OfferEventEnvelope(
        String eventType,
        String aggregateId,
        int version,
        Instant occurredAt,
        DomainEvent payload
) {

    public static OfferEventEnvelope wrap(DomainEvent event) {
        return new OfferEventEnvelope(
                event.getClass().getSimpleName(),
                event.aggregateId(),
                event.version(),
                event.occurredAt(),
                event
        );
    }
}
