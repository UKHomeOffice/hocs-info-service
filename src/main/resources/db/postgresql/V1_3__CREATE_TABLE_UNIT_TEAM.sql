DROP TABLE IF EXISTS unit;

CREATE TABLE IF NOT EXISTS unit
(
  id           BIGSERIAL PRIMARY KEY,
  display_name TEXT NOT NULL,
  uuid         UUID NOT NULL,
  short_code   TEXT NOT NULL,
  active      boolean NOT NULL,

  CONSTRAINT unit_uuid_idempotent UNIQUE (uuid),
  CONSTRAINT unit_name_idempotent UNIQUE (display_name),
  CONSTRAINT unit_code_idempotent UNIQUE (short_code)
);

CREATE INDEX idx_unit_uuid
  ON unit (uuid);

CREATE INDEX idx_unit_active
  ON unit (active);


DROP TABLE IF EXISTS team;

CREATE TABLE IF NOT EXISTS team
(
  id           BIGSERIAL PRIMARY KEY,
  display_name TEXT NOT NULL,
  uuid         UUID NOT NULL,
  unit_uuid    UUID NOT NULL,

  CONSTRAINT team_uuid_idempotent UNIQUE (uuid),
  CONSTRAINT team_name_idempotent UNIQUE (display_name),
  CONSTRAINT fk_team_id FOREIGN KEY (unit_uuid) REFERENCES unit (uuid)
);

CREATE INDEX idx_team_uuid
  ON team (uuid);

CREATE INDEX idx_team_unit_uuid
  ON team (unit_uuid);

DROP TABLE IF EXISTS unit_case_type;

CREATE TABLE IF NOT EXISTS unit_case_type
(
  id          BIGSERIAL PRIMARY KEY,
  unit_uuid   UUID    NOT NULL,
  case_type TEXT NOT NULL,

  CONSTRAINT fk_tenant_unit_case_type FOREIGN KEY (case_type) REFERENCES case_type (type),
  CONSTRAINT fk_tenant_unit_unit_uuid FOREIGN KEY (unit_uuid) REFERENCES unit (uuid)

);

CREATE INDEX idx_tenant_team_unit_uuid
  ON unit_case_type(unit_uuid);

CREATE INDEX idx_tenant_team_case_type
  ON unit_case_type(case_type);

