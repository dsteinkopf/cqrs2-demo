package com.redteclab.cqrs2.command.domain;

import com.redteclab.cqrs2.shared.event.DomainEvent;
import com.redteclab.cqrs2.shared.event.OfferCreatedEvent;
import com.redteclab.cqrs2.shared.event.OfferDeactivatedEvent;
import com.redteclab.cqrs2.shared.event.OfferUpdatedEvent;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public class OfferAggregate {

    private String offerId;
    private String title;
    private BigDecimal price;
    private boolean active;
    private int version;

    private OfferAggregate() {
    }

    public static OfferCreatedEvent create(String title, BigDecimal price) {
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("Title must not be empty");
        }
        if (price == null || price.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Price must be greater than zero");
        }
        return new OfferCreatedEvent(
                UUID.randomUUID().toString(),
                1,
                Instant.now(),
                title,
                price
        );
    }

    public OfferUpdatedEvent update(String title, BigDecimal price) {
        if (!active) {
            throw new IllegalStateException("Cannot update a deactivated offer");
        }
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("Title must not be empty");
        }
        if (price == null || price.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Price must be greater than zero");
        }
        return new OfferUpdatedEvent(offerId, version + 1, Instant.now(), title, price);
    }

    public OfferDeactivatedEvent deactivate() {
        if (!active) {
            throw new IllegalStateException("Offer is already deactivated");
        }
        return new OfferDeactivatedEvent(offerId, version + 1, Instant.now());
    }

    public static OfferAggregate rehydrate(List<DomainEvent> events) {
        var aggregate = new OfferAggregate();
        for (DomainEvent event : events) {
            aggregate.apply(event);
        }
        return aggregate;
    }

    private void apply(DomainEvent event) {
        switch (event) {
            case OfferCreatedEvent e -> {
                this.offerId = e.aggregateId();
                this.title = e.title();
                this.price = e.price();
                this.active = true;
                this.version = e.version();
            }
            case OfferUpdatedEvent e -> {
                this.title = e.title();
                this.price = e.price();
                this.version = e.version();
            }
            case OfferDeactivatedEvent e -> {
                this.active = false;
                this.version = e.version();
            }
            default -> throw new IllegalArgumentException("Unknown event type: " + event.getClass());
        }
    }

    public String offerId() {
        return offerId;
    }

    public int version() {
        return version;
    }
}
