CREATE TABLE IF NOT EXISTS event_store (
    stream_id   VARCHAR(255)    NOT NULL,
    sequence    INT             NOT NULL,
    event_type  VARCHAR(64)     NOT NULL,
    payload     JSONB           NOT NULL,
    occurred_at TIMESTAMPTZ     NOT NULL,
    PRIMARY KEY (stream_id, sequence)
);
