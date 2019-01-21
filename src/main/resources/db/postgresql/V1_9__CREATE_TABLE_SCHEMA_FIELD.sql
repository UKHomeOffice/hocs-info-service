DROP TABLE IF EXISTS screen_schema;

CREATE TABLE IF NOT EXISTS screen_schema
(
  id     BIGSERIAL PRIMARY KEY,
  uuid   UUID    NOT NULL,
  type   TEXT    NOT NULL,
  title  TEXT    NOT NULL,
  action_label TEXT NOT NULL,
  active BOOLEAN NOT NULL DEFAULT TRUE,

  CONSTRAINT schema_uuid_idempotent UNIQUE (uuid),
  CONSTRAINT form_type_idempotent UNIQUE (type)
);


DROP TABLE IF EXISTS stage_type_schema;

CREATE TABLE IF NOT EXISTS stage_type_schema
(
  id              BIGSERIAL PRIMARY KEY,
  stage_type_uuid UUID NOT NULL,
  schema_uuid     UUID NOT NULL,

  CONSTRAINT fk_form_stage_type FOREIGN KEY (stage_type_uuid) REFERENCES stage_type (uuid),
  CONSTRAINT fk_form_form FOREIGN KEY (schema_uuid) REFERENCES screen_schema (uuid)
);

DROP TABLE IF EXISTS case_type_schema;

CREATE TABLE IF NOT EXISTS case_type_schema
(
  id          BIGSERIAL PRIMARY KEY,
  case_type   TEXT NOT NULL,
  schema_uuid   UUID    NOT NULL,

  CONSTRAINT fk_case_type_schema_case_type FOREIGN KEY (case_type) REFERENCES case_type (type),
  CONSTRAINT fk_case_type_schema_uuid FOREIGN KEY (schema_uuid) REFERENCES screen_schema (uuid)

);

CREATE INDEX idx_case_type_schema_case_type
  ON case_type_schema(case_type);


DROP TABLE IF EXISTS field;

CREATE TABLE IF NOT EXISTS field
(
  id     BIGSERIAL PRIMARY KEY,
  uuid   UUID    NOT NULL,
  component TEXT NOT NULL,
  name      TEXT,
  label     TEXT,
  validation JSONB NOT NULL DEFAULT '{}',
  props JSONB DEFAULT '{}',
  summary BOOLEAN NOT NULL DEFAULT FALSE,
  active BOOLEAN NOT NULL DEFAULT TRUE,

  CONSTRAINT field_uuid_idempotent UNIQUE (uuid)
);

DROP TABLE IF EXISTS field_screen;

CREATE TABLE IF NOT EXISTS field_screen
(
  schema_uuid   UUID    NOT NULL,
  field_uuid   UUID    NOT NULL,

    CONSTRAINT fk_field_case_type_uuid FOREIGN KEY (schema_uuid) REFERENCES screen_schema (uuid)

);
