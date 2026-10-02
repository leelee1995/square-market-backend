-- V1__create_all_tables.sql
CREATE TABLE neighbor
(
    id                      UUID PRIMARY KEY,
    username                VARCHAR(255)             NOT NULL UNIQUE,
    first_name              VARCHAR(255)             NOT NULL,
    last_name               VARCHAR(255),
    email                   VARCHAR(255)             NOT NULL UNIQUE,
    password                VARCHAR(255)             NOT NULL,
    country                 VARCHAR(255),
    administrative_division VARCHAR(255),
    municipality            VARCHAR(255),
    created_at              TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at              TIMESTAMP WITH TIME ZONE NOT NULL,
    deleted_at              TIMESTAMP WITH TIME ZONE
);


CREATE TABLE listing
(
    id                      UUID PRIMARY KEY,
    title                   TEXT                     NOT NULL,
    details                 TEXT,
    neighbor_id             UUID                     NOT NULL,
    price                   BIGINT                   NOT NULL DEFAULT 0,
    images                  TEXT[] NOT NULL DEFAULT '{}',
    status                  VARCHAR(50)              NOT NULL,
    condition               VARCHAR(50)              NOT NULL,
    category                VARCHAR(50)              NOT NULL,
    municipality            VARCHAR(255)             NOT NULL,
    administrative_division VARCHAR(255)             NOT NULL,
    country                 VARCHAR(255)             NOT NULL,
    created_at              TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at              TIMESTAMP WITH TIME ZONE NOT NULL,
    deleted_at              TIMESTAMP WITH TIME ZONE,

    CONSTRAINT fk_listing_neighbor
        FOREIGN KEY (neighbor_id)
            REFERENCES neighbor (id)
            ON DELETE CASCADE
);


CREATE TABLE wanted_post
(
    id          UUID PRIMARY KEY,
    summary     TEXT                     NOT NULL,
    details     TEXT,
    images      TEXT[] NOT NULL DEFAULT '{}',
    min_price   BIGINT                   NOT NULL DEFAULT 0,
    max_price   BIGINT                   NOT NULL DEFAULT 0,
    neighbor_id UUID                     NOT NULL,
    created_at  TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at  TIMESTAMP WITH TIME ZONE NOT NULL,
    deleted_at  TIMESTAMP WITH TIME ZONE,

    CONSTRAINT fk_wanted_post_neighbor
        FOREIGN KEY (neighbor_id)
            REFERENCES neighbor (id)
            ON DELETE CASCADE
);


CREATE TABLE wanted_post_item
(
    id             UUID PRIMARY KEY,
    wanted_post_id UUID        NOT NULL,
    title          TEXT        NOT NULL,
    status         VARCHAR(50) NOT NULL,
    condition      VARCHAR(50) NOT NULL DEFAULT 'ANY',

    CONSTRAINT fk_wanted_post_item_post
        FOREIGN KEY (wanted_post_id)
            REFERENCES wanted_post (id)
            ON DELETE CASCADE
);


CREATE INDEX IF NOT EXISTS idx_listing_neighbor_id ON listing (neighbor_id);
CREATE INDEX IF NOT EXISTS idx_wanted_post_neighbor_id ON wanted_post (neighbor_id);
CREATE INDEX IF NOT EXISTS idx_wanted_post_item_wanted_post_id ON wanted_post_item (wanted_post_id);

CREATE INDEX IF NOT EXISTS idx_listing_status ON listing (status);