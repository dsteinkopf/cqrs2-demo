CREATE TABLE IF NOT EXISTS offer_projection (
    offer_id    VARCHAR(255)    PRIMARY KEY,
    title       VARCHAR(255)    NOT NULL,
    price       NUMERIC(10,2)   NOT NULL,
    active      BOOLEAN         NOT NULL DEFAULT TRUE,
    version     INT             NOT NULL,
    updated_at  TIMESTAMPTZ     NOT NULL
);
