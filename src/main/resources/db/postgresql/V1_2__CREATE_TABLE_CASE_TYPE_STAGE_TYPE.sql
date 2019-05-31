DROP TABLE IF EXISTS case_type;

CREATE TABLE IF NOT EXISTS case_type
(
  id               BIGSERIAL PRIMARY KEY,
  uuid             UUID    NOT NULL,
  display_name     TEXT    NOT NULL,
  short_code       TEXT    NOT NULL,
  type             TEXT    NOT NULL,
  owning_unit_uuid        UUID    NOT NULL,
  deadline_stage   TEXT    NOT NULL,
  bulk             boolean NOT NULL DEFAULT TRUE,
  active           boolean NOT NULL DEFAULT TRUE,

  CONSTRAINT case_type_uuid_idempotent UNIQUE (uuid),
  CONSTRAINT case_type_name_idempotent UNIQUE (display_name),
  CONSTRAINT case_type_type_idempotent UNIQUE (type),
  CONSTRAINT case_type_short_idempotent UNIQUE (short_code),
  CONSTRAINT fk_case_type_unit_uuid FOREIGN KEY (owning_unit_uuid) REFERENCES unit (uuid)

);

CREATE INDEX idx_case_type_unit_uuid
  ON case_type (owning_unit_uuid, active);

CREATE INDEX idx_case_type_active
  ON case_type (active);

CREATE INDEX idx_case_type_bulk_active
  ON case_type (bulk, active);


DROP TABLE IF EXISTS stage_type;

CREATE TABLE IF NOT EXISTS stage_type
(
  id                    BIGSERIAL PRIMARY KEY,
  uuid                  UUID    NOT NULL,
  display_name          TEXT    NOT NULL,
  short_code            TEXT    NOT NULL,
  type                  TEXT    NOT NULL,
  case_type_uuid        UUID    NOT NULL,
  acting_team_uuid      UUID    ,
  deadline              int     NOT NULL,
  active                boolean NOT NULL,

  CONSTRAINT stage_type_uuid_idempotent UNIQUE (uuid),
  CONSTRAINT stage_type_type_idempotent UNIQUE (type),
  CONSTRAINT stage_type_short_idempotent UNIQUE (short_code),
  CONSTRAINT fk_stage_type_case_type_uuid FOREIGN KEY (case_type_uuid) REFERENCES case_type (uuid),
  CONSTRAINT fk_stage_type_team_uuid FOREIGN KEY (acting_team_uuid) REFERENCES team (uuid)
);

CREATE INDEX idx_stage_type_active
  ON stage_type (type, active);

DROP TABLE IF EXISTS exemption_date;

CREATE TABLE IF NOT EXISTS exemption_date
(
  id             BIGSERIAL PRIMARY KEY,
  uuid           UUID NOT NULL,
  date           DATE NOT NULL,
  case_type_uuid UUID NOT NULL,

  CONSTRAINT exemption_date_uuid_idempotent UNIQUE (uuid),
  CONSTRAINT exemption_date_idempotent UNIQUE (date, case_type_uuid),
  CONSTRAINT fk_exemption_date_case_uuid FOREIGN KEY (case_type_uuid) REFERENCES case_type (uuid)
);

CREATE INDEX idx_exemption_date_case_type
  ON exemption_date (case_type_uuid);