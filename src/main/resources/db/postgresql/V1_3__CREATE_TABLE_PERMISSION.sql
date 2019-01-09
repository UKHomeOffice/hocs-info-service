DROP TABLE IF EXISTS permission;

CREATE TABLE IF NOT EXISTS permission
(
  id           BIGSERIAL PRIMARY KEY,
  team_uuid    UUID NOT NULL,
  case_type    TEXT NOT NULL,
  access_level TEXT NOT NULL,

  CONSTRAINT fk_permission_case_type FOREIGN KEY (case_type) REFERENCES case_type (type),
  CONSTRAINT fk_permission_team_uuid FOREIGN KEY (team_uuid) REFERENCES team (uuid)

);