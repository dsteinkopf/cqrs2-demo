package com.redteclab.cqrs2.command.infrastructure.persistence;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.redteclab.cqrs2.command.application.EventStore;
import com.redteclab.cqrs2.shared.event.DomainEvent;
import com.redteclab.cqrs2.shared.event.OfferCreatedEvent;
import com.redteclab.cqrs2.shared.event.OfferDeactivatedEvent;
import com.redteclab.cqrs2.shared.event.OfferUpdatedEvent;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Component
public class JpaEventStore implements EventStore {

    private static final Map<String, Class<? extends DomainEvent>> EVENT_TYPES = Map.of(
            "OfferCreatedEvent", OfferCreatedEvent.class,
            "OfferUpdatedEvent", OfferUpdatedEvent.class,
            "OfferDeactivatedEvent", OfferDeactivatedEvent.class
    );

    private final EventStoreRepository repository;
    private final ObjectMapper objectMapper;

    public JpaEventStore(EventStoreRepository repository, ObjectMapper objectMapper) {
        this.repository = repository;
        this.objectMapper = objectMapper;
    }

    @Override
    @Transactional(readOnly = true)
    public List<DomainEvent> loadEvents(String aggregateId) {
        return repository.findByStreamIdOrderBySequenceAsc(aggregateId)
                .stream()
                .map(this::deserialize)
                .toList();
    }

    @Override
    @Transactional
    public void append(String aggregateId, int expectedVersion, DomainEvent event) {
        var entities = repository.findByStreamIdOrderBySequenceAsc(aggregateId);
        int currentVersion = entities.isEmpty() ? 0 : entities.getLast().sequence();

        if (currentVersion != expectedVersion) {
            throw new IllegalStateException(
                    "Concurrency conflict: expected version %d but found %d"
                            .formatted(expectedVersion, currentVersion));
        }

        var entity = new EventStoreEntity(
                aggregateId,
                event.version(),
                event.getClass().getSimpleName(),
                serialize(event),
                event.occurredAt()
        );
        repository.save(entity);
    }

    private String serialize(DomainEvent event) {
        try {
            return objectMapper.writeValueAsString(event);
        } catch (Exception e) {
            throw new RuntimeException("Failed to serialize event", e);
        }
    }

    private DomainEvent deserialize(EventStoreEntity entity) {
        Class<? extends DomainEvent> eventClass = EVENT_TYPES.get(entity.eventType());
        if (eventClass == null) {
            throw new IllegalArgumentException("Unknown event type: " + entity.eventType());
        }
        try {
            return objectMapper.readValue(entity.payload(), eventClass);
        } catch (Exception e) {
            throw new RuntimeException("Failed to deserialize event", e);
        }
    }
}
