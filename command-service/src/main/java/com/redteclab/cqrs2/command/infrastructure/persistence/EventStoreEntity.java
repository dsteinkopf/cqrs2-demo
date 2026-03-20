package com.redteclab.cqrs2.command.infrastructure.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import java.time.Instant;

@Entity
@Table(name = "event_store")
@IdClass(EventStoreId.class)
public class EventStoreEntity {

    @Id
    @Column(name = "stream_id", nullable = false)
    private String streamId;

    @Id
    @Column(name = "sequence", nullable = false)
    private int sequence;

    @Column(name = "event_type", nullable = false, length = 64)
    private String eventType;

    @Column(name = "payload", nullable = false, columnDefinition = "jsonb")
    @JdbcTypeCode(SqlTypes.JSON)
    private String payload;

    @Column(name = "occurred_at", nullable = false)
    private Instant occurredAt;

    protected EventStoreEntity() {
    }

    public EventStoreEntity(String streamId, int sequence, String eventType,
                            String payload, Instant occurredAt) {
        this.streamId = streamId;
        this.sequence = sequence;
        this.eventType = eventType;
        this.payload = payload;
        this.occurredAt = occurredAt;
    }

    public String streamId() {
        return streamId;
    }

    public int sequence() {
        return sequence;
    }

    public String eventType() {
        return eventType;
    }

    public String payload() {
        return payload;
    }

    public Instant occurredAt() {
        return occurredAt;
    }
}
