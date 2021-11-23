DROP TABLE IF EXISTS parent_topic;

CREATE TABLE IF NOT EXISTS parent_topic
(
  id           BIGSERIAL PRIMARY KEY,
  uuid         UUID NOT NULL,
  display_name TEXT NOT NULL,
  active       boolean NOT NULL DEFAULT TRUE,

  CONSTRAINT parent_topic_uuid_idempotent UNIQUE (uuid),
  CONSTRAINT parent_topic_name_idempotent UNIQUE (display_name)

);

CREATE INDEX idx_parent_topic_uuid
  ON parent_topic (uuid);
CREATE INDEX idx_parent_topic_all
  ON parent_topic (display_name, uuid);


DROP TABLE IF EXISTS topic;

CREATE TABLE IF NOT EXISTS topic
(
  id                BIGSERIAL PRIMARY KEY,
  display_name      TEXT NOT NULL,
  uuid              UUID NOT NULL,
  parent_topic_uuid UUID NOT NULL,
  active            boolean NOT NULL DEFAULT TRUE,

  CONSTRAINT topic_uuid_idempotent UNIQUE (uuid),
  CONSTRAINT topic_name_idempotent UNIQUE (display_name, parent_topic_uuid)
);

CREATE INDEX idx_topic_uuid
  ON topic (uuid);
CREATE INDEX idx_topic_all
  ON topic (display_name, uuid, parent_topic_uuid);

DROP TABLE IF EXISTS team_link;

CREATE TABLE IF NOT EXISTS team_link
(
  id                    BIGSERIAL PRIMARY KEY,
  case_type             TEXT      NOT NULL,
  stage_type            TEXT      NOT NULL,
  link_uuid             UUID      NOT NULL,
  link_type             TEXT      NOT NULL, -- e.g. 'TOPIC', 'UNIT', 'CONSTITUENCY'
  responsible_team_uuid UUID      NOT NULL,

  CONSTRAINT fk_team_link_case_type FOREIGN KEY (case_type) REFERENCES case_type (type),
  CONSTRAINT fk_team_link_stage_type FOREIGN KEY (stage_type) REFERENCES stage_type (type),
  CONSTRAINT fk_team_link_team_uuid FOREIGN KEY (responsible_team_uuid) REFERENCES team (uuid)
);

CREATE INDEX idx_team_link_case_stage_link
  ON team_link (case_type, stage_type, link_uuid);

