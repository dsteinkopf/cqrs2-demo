package com.redteclab.cqrs2.command.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EventStoreRepository extends JpaRepository<EventStoreEntity, EventStoreId> {

    List<EventStoreEntity> findByStreamIdOrderBySequenceAsc(String streamId);
}
