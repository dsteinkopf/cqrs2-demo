package com.redteclab.cqrs2.command.application;

import com.redteclab.cqrs2.shared.event.DomainEvent;
import java.util.List;

public interface EventStore {

    List<DomainEvent> loadEvents(String aggregateId);

    void append(String aggregateId, int expectedVersion, DomainEvent event);
}
