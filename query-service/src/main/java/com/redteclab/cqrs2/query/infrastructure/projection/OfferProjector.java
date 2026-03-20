package com.redteclab.cqrs2.query.infrastructure.projection;

import com.redteclab.cqrs2.query.infrastructure.persistence.OfferProjectionEntity;
import com.redteclab.cqrs2.query.infrastructure.persistence.OfferProjectionRepository;
import com.redteclab.cqrs2.shared.event.OfferCreatedEvent;
import com.redteclab.cqrs2.shared.event.OfferDeactivatedEvent;
import com.redteclab.cqrs2.shared.event.OfferUpdatedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class OfferProjector {

    private static final Logger LOG = LoggerFactory.getLogger(OfferProjector.class);

    private final OfferProjectionRepository repository;

    public OfferProjector(OfferProjectionRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public void on(OfferCreatedEvent event) {
        var entity = new OfferProjectionEntity(
                event.aggregateId(),
                event.title(),
                event.price(),
                true,
                event.version(),
                event.occurredAt()
        );
        repository.save(entity);
        LOG.info("Projected OfferCreated: {}", event.aggregateId());
    }

    @Transactional
    public void on(OfferUpdatedEvent event) {
        var entity = repository.findById(event.aggregateId())
                .orElseThrow(() -> new IllegalStateException(
                        "Projection not found for: " + event.aggregateId()));
        entity.setTitle(event.title());
        entity.setPrice(event.price());
        entity.setVersion(event.version());
        entity.setUpdatedAt(event.occurredAt());
        repository.save(entity);
        LOG.info("Projected OfferUpdated: {}", event.aggregateId());
    }

    @Transactional
    public void on(OfferDeactivatedEvent event) {
        var entity = repository.findById(event.aggregateId())
                .orElseThrow(() -> new IllegalStateException(
                        "Projection not found for: " + event.aggregateId()));
        entity.setActive(false);
        entity.setVersion(event.version());
        entity.setUpdatedAt(event.occurredAt());
        repository.save(entity);
        LOG.info("Projected OfferDeactivated: {}", event.aggregateId());
    }
}
