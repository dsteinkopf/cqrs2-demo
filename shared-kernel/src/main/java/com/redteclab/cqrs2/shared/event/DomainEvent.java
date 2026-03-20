package com.redteclab.cqrs2.shared.event;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import java.time.Instant;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "@type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = OfferCreatedEvent.class, name = "OfferCreatedEvent"),
        @JsonSubTypes.Type(value = OfferUpdatedEvent.class, name = "OfferUpdatedEvent"),
        @JsonSubTypes.Type(value = OfferDeactivatedEvent.class, name = "OfferDeactivatedEvent")
})
public interface DomainEvent {

    String aggregateId();

    int version();

    Instant occurredAt();
}
