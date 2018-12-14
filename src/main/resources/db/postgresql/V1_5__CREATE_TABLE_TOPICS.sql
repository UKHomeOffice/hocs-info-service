DROP TABLE IF EXISTS parent_topic;

CREATE TABLE IF NOT EXISTS parent_topic
(
  id           BIGSERIAL PRIMARY KEY,
  display_name TEXT NOT NULL,
  uuid         UUID NOT NULL,

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

  CONSTRAINT topic_uuid_idempotent UNIQUE (uuid),
  CONSTRAINT topic_name_idempotent UNIQUE (display_name),
  CONSTRAINT fk_topic_id FOREIGN KEY (parent_topic_uuid) REFERENCES parent_topic (uuid)

);

CREATE INDEX idx_topic_uuid
  ON topic (uuid);
CREATE INDEX idx_topic_all
  ON topic (display_name, uuid, parent_topic_uuid);

DROP TABLE IF EXISTS parent_topic_team;

CREATE TABLE IF NOT EXISTS parent_topic_team
(
  id                BIGSERIAL PRIMARY KEY,
  parent_topic_uuid UUID    NOT NULL,
  tenant_role       TEXT NOT NULL,
  team_uuid         UUID    NOT NULL,
  active            boolean NOT NULL,

  CONSTRAINT fk_parent_topic_team_uuid FOREIGN KEY (parent_topic_uuid) REFERENCES parent_topic (uuid),
  CONSTRAINT fk_parent_topic_team_tenant_role FOREIGN KEY (tenant_role) REFERENCES tenant (role),
  CONSTRAINT fk_parent_topic_team_team_id FOREIGN KEY (team_uuid) REFERENCES team (uuid)

);

CREATE INDEX idx_parent_topic_active
  ON parent_topic_team (parent_topic_uuid, tenant_role, team_uuid, active);