DROP TABLE IF EXISTS unit;

CREATE TABLE IF NOT EXISTS unit
(
  id           BIGSERIAL PRIMARY KEY,
  display_name TEXT NOT NULL,
  uuid         UUID NOT NULL,

  CONSTRAINT unit_uuid_idempotent UNIQUE (uuid),
  CONSTRAINT unit_name_idempotent UNIQUE (display_name)

);

CREATE INDEX idx_unit_uuid
  ON unit (uuid);
CREATE INDEX idx_unit_all
  ON unit (display_name, uuid);


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
CREATE INDEX idx_team_all
  ON team (display_name, uuid, unit_uuid);

DROP TABLE IF EXISTS tenant_unit;

CREATE TABLE IF NOT EXISTS tenant_unit
(
  id          BIGSERIAL PRIMARY KEY,
  tenant_role TEXT NOT NULL,
  unit_uuid   UUID    NOT NULL,
  active      boolean NOT NULL,

  CONSTRAINT fk_tenant_unit_tenant_role FOREIGN KEY (tenant_role) REFERENCES tenant (role),
  CONSTRAINT fk_tenant_unit_unit_uuid FOREIGN KEY (unit_uuid) REFERENCES unit (uuid)

);

CREATE INDEX idx_team_active
  ON tenant_unit (tenant_role, unit_uuid, active);