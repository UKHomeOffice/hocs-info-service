DROP TABLE IF EXISTS nominated_person;

CREATE TABLE IF NOT EXISTS nominated_person
(
  id       BIGSERIAL PRIMARY KEY,
  team_uuid UUID NOT NULL,
  email_address    TEXT NOT NULL,

  CONSTRAINT fk_team_id FOREIGN KEY (team_uuid) REFERENCES team (uuid)
);
