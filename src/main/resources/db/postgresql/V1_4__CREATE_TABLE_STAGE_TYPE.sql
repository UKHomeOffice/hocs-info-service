DROP TABLE IF EXISTS stage_type;

CREATE TABLE IF NOT EXISTS stage_type
(
  id           BIGSERIAL PRIMARY KEY,
  display_name TEXT    NOT NULL,
  short_code   TEXT    NOT NULL,
  type         TEXT    NOT NULL,
  team_uuid    UUID    NOT NULL,
  deadline     INT     NOT NULL,
  active       boolean NOT NULL,

  CONSTRAINT stage_type_type_idempotent UNIQUE ( type ),
  CONSTRAINT stage_type_short_idempotent UNIQUE (short_code),
  CONSTRAINT fk_team_uuid FOREIGN KEY (team_uuid) REFERENCES team (uuid)
  );

CREATE INDEX idx_stage_type_active
ON stage_type (active);


CREATE OR REPLACE VIEW sla AS
SELECT id as id, type as stage_type, deadline as value, team_uuid as case_type
FROM stage_type
WHERE deadline <> 0;