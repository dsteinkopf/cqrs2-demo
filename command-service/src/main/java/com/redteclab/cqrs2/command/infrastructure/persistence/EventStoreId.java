package com.redteclab.cqrs2.command.infrastructure.persistence;

import java.io.Serializable;
import java.util.Objects;

public class EventStoreId implements Serializable {

    private String streamId;
    private int sequence;

    public EventStoreId() {
    }

    public EventStoreId(String streamId, int sequence) {
        this.streamId = streamId;
        this.sequence = sequence;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof EventStoreId that)) return false;
        return sequence == that.sequence && Objects.equals(streamId, that.streamId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(streamId, sequence);
    }
}
