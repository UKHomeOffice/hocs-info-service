DROP TABLE IF EXISTS secondary_action;

CREATE TABLE IF NOT EXISTS secondary_action
(
    id     BIGSERIAL PRIMARY KEY,
    uuid   UUID    NOT NULL,
    component TEXT NOT NULL,
    name      TEXT,
    label     TEXT,
    validation JSONB NOT NULL DEFAULT '{}',
    props JSONB DEFAULT '{}',

    CONSTRAINT sec_action_uuid_idempotent UNIQUE (uuid)
);

DROP TABLE IF EXISTS secondary_action_screen;

CREATE TABLE IF NOT EXISTS secondary_action_screen
(
    schema_uuid   UUID    NOT NULL,
    secondary_action_uuid   UUID    NOT NULL,

    CONSTRAINT fk_secondary_action_schema_uuid FOREIGN KEY (schema_uuid) REFERENCES screen_schema (uuid),
    CONSTRAINT fk_secondary_action_uuid FOREIGN KEY (secondary_action_uuid) REFERENCES secondary_action (uuid)

);

