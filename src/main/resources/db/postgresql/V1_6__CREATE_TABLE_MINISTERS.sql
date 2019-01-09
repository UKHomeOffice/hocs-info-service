DROP TABLE IF EXISTS minister;

CREATE TABLE IF NOT EXISTS minister
(
  id            BIGSERIAL PRIMARY KEY,
  uuid          UUID    NOT NULL,
  office_name   TEXT    NOT NULL,
  minister_name TEXT    NOT NULL,
  responsible_team_uuid     UUID    NOT NULL,

  CONSTRAINT minister_uuid_idempotent UNIQUE (uuid),
  CONSTRAINT minister_name_idempotent UNIQUE (office_name),
  CONSTRAINT fk_minister_team_id FOREIGN KEY (responsible_team_uuid) REFERENCES team (uuid)

);

