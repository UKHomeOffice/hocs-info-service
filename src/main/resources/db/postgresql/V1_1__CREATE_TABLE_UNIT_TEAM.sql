DROP TABLE IF EXISTS unit;

CREATE TABLE IF NOT EXISTS unit
(
  id           BIGSERIAL PRIMARY KEY,
  uuid         UUID    NOT NULL,
  display_name TEXT    NOT NULL,
  short_code   TEXT    NOT NULL,
  active       boolean NOT NULL DEFAULT TRUE,

  CONSTRAINT unit_uuid_idempotent UNIQUE (uuid),
  CONSTRAINT unit_name_idempotent UNIQUE (display_name),
  CONSTRAINT unit_code_idempotent UNIQUE (short_code)
);

CREATE INDEX idx_unit_uuid
  ON unit (uuid, active);

DROP TABLE IF EXISTS team;

CREATE TABLE IF NOT EXISTS team
(
  id           BIGSERIAL PRIMARY KEY,
  uuid         UUID    NOT NULL,
  display_name TEXT    NOT NULL,
  letter_name TEXT,
  unit_uuid    UUID    NOT NULL,
  active       boolean NOT NULL DEFAULT TRUE,

  CONSTRAINT team_uuid_idempotent UNIQUE (uuid),
  CONSTRAINT team_name_idempotent UNIQUE (display_name),
  CONSTRAINT fk_team_id FOREIGN KEY (unit_uuid) REFERENCES unit (uuid)
);

CREATE INDEX idx_team_uuid
  ON team (uuid, active);

CREATE INDEX idx_team_unit_uuid
  ON team (unit_uuid, active);

DROP TABLE IF EXISTS team_contact;

CREATE TABLE IF NOT EXISTS team_contact
(
  id            BIGSERIAL PRIMARY KEY,
  uuid          UUID NOT NULL,
  team_uuid     UUID NOT NULL,
  email_address TEXT NOT NULL,

  CONSTRAINT team_contact_idempotent UNIQUE (uuid),
  CONSTRAINT team_contact_details_idempotent UNIQUE (team_uuid, email_address),
  CONSTRAINT fk_team_contact_id FOREIGN KEY (team_uuid) REFERENCES team (uuid)
);