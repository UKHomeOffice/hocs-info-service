DROP TABLE IF EXISTS parent_topic;

CREATE TABLE IF NOT EXISTS parent_topic
(
  id           BIGSERIAL PRIMARY KEY,
  uuid         UUID NOT NULL,
  display_name TEXT NOT NULL,
  responsible_team_uuid         UUID    NOT NULL,
  responsible_minister_uuid UUID NOT NULL,
  active  boolean NOT NULL DEFAULT TRUE,

  CONSTRAINT parent_topic_uuid_idempotent UNIQUE (uuid),
  CONSTRAINT parent_topic_name_idempotent UNIQUE (display_name),
  CONSTRAINT fk_parent_topic_team_id FOREIGN KEY (responsible_team_uuid) REFERENCES team (uuid)

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

  CONSTRAINT topic_uuid_idempotent UNIQUE (uuid),
  CONSTRAINT topic_name_idempotent UNIQUE (display_name),
  CONSTRAINT fk_topic_id FOREIGN KEY (parent_topic_uuid) REFERENCES parent_topic (uuid)

);

CREATE INDEX idx_topic_uuid
  ON topic (uuid);
CREATE INDEX idx_topic_all
  ON topic (display_name, uuid, parent_topic_uuid);

DROP TABLE IF EXISTS parent_topic_case_type;

CREATE TABLE IF NOT EXISTS parent_topic_case_type
(
  id                BIGSERIAL PRIMARY KEY,
  parent_topic_uuid      UUID    NOT NULL,
  case_type      TEXT NOT NULL,

  CONSTRAINT fk_parent_topic_case_type FOREIGN KEY (case_type) REFERENCES case_type (type),
  CONSTRAINT fk_parent_topic_unit_uuid FOREIGN KEY (parent_topic_uuid) REFERENCES parent_topic (uuid)

);