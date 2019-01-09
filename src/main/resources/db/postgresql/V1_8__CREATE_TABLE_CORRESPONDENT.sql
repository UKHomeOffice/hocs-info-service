DROP TABLE IF EXISTS correspondent_type;

CREATE TABLE IF NOT EXISTS correspondent_type
(
  id           BIGSERIAL PRIMARY KEY,
  uuid         UUID    NOT NULL,
  display_name TEXT NOT NULL,
  type         TEXT NOT NULL,

  CONSTRAINT correspondent_type_type_idempotent UNIQUE (type),
  CONSTRAINT correspondent_type_name_idempotent UNIQUE (display_name)
);
