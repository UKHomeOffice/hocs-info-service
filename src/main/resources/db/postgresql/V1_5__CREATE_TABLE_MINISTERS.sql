DROP TABLE IF EXISTS minister;

CREATE TABLE IF NOT EXISTS minister
(
  id            BIGSERIAL PRIMARY KEY,
  office_name   TEXT    NOT NULL,
  minister_name TEXT    NOT NULL,
  uuid          UUID    NOT NULL,

  CONSTRAINT minister_uuid_idempotent UNIQUE (uuid),
  CONSTRAINT minister_name_idempotent UNIQUE (office_name)
);

CREATE INDEX idx_minister_office
  ON minister (office_name, uuid);
CREATE INDEX idx_minister_name
  ON minister (minister_name, uuid);

DROP TABLE IF EXISTS minister_team;

CREATE TABLE IF NOT EXISTS minister_team
(
  id            BIGSERIAL PRIMARY KEY,
  minister_UUID UUID    NOT NULL,
  tenant_role   TEXT NOT NULL,
  team_uuid     UUID    NOT NULL,
  active        boolean NOT NULL,

  CONSTRAINT fk_minister_team_minister_id FOREIGN KEY (minister_uuid) REFERENCES minister (uuid),
  CONSTRAINT fk_minister_team_tenant_role FOREIGN KEY (tenant_role) REFERENCES tenant (role),
  CONSTRAINT fk_minister_team_team_id FOREIGN KEY (team_uuid) REFERENCES team (uuid)

);

CREATE INDEX idx_minister_team_active
  ON minister_team (minister_UUID, tenant_role, team_uuid, active);


CREATE TABLE IF NOT EXISTS parent_topic_minister
(
  id            BIGSERIAL PRIMARY KEY,
  minister_UUID UUID    NOT NULL,
  tenant_role   TEXT NOT NULL,
  parent_topic_uuid     UUID    NOT NULL,
  active        boolean NOT NULL,

  CONSTRAINT fk_parent_topic_minister_minister_id FOREIGN KEY (minister_uuid) REFERENCES minister (uuid),
  CONSTRAINT fk_parent_topic_minister_tenant_role FOREIGN KEY (tenant_role) REFERENCES tenant (role),
  CONSTRAINT fk_parent_topic_minister_topic_id FOREIGN KEY (parent_topic_uuid) REFERENCES parent_topic (uuid)

);

CREATE INDEX idx_parent_topic_minister_active
  ON parent_topic_minister (minister_UUID, tenant_role, parent_topic_uuid, active);