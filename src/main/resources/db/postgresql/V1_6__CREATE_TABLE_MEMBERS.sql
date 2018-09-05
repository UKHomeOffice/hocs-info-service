DROP TABLE IF EXISTS member;

CREATE TABLE IF NOT EXISTS member
(
  id           BIGSERIAL PRIMARY KEY,
  display_name TEXT    NOT NULL,
  list_as      TEXT    NOT NULL,
  full_title   TEXT    NOT NULL,
  external_id  TEXT    NOT NULL,
  house        TEXT    NOT NULL,
  uuid         UUID    NOT NULL,
  active       boolean NOT NULL,

  CONSTRAINT member_external_id_idempotent UNIQUE (external_id, house),
  CONSTRAINT member_uuid_idempotent UNIQUE (uuid),
  CONSTRAINT member_name_idempotent UNIQUE (display_name)

);

CREATE INDEX idx_member_active
  ON member (active);


DROP TABLE IF EXISTS member_team;

CREATE TABLE IF NOT EXISTS member_team
(
  id          BIGSERIAL PRIMARY KEY,
  member_UUID UUID NOT NULL,
  tenant_role TEXT NOT NULL,
  team_uuid   UUID NOT NULL,

  CONSTRAINT fk_member_team_member_id FOREIGN KEY (member_uuid) REFERENCES member (uuid),
  CONSTRAINT fk_member_tenant_role FOREIGN KEY (tenant_role) REFERENCES tenant (role),
  CONSTRAINT fk_member_team_team_id FOREIGN KEY (team_uuid) REFERENCES team (uuid)

);

DROP TABLE IF EXISTS member_case_type;

CREATE TABLE IF NOT EXISTS member_case_type
(
  id          BIGSERIAL PRIMARY KEY,
  member_uuid UUID NOT NULL,
  case_type   TEXT NOT NULL,

  CONSTRAINT fk_member_case_type_uuid FOREIGN KEY (member_uuid) REFERENCES member (uuid),
  CONSTRAINT fk_member_case_type_type FOREIGN KEY (case_type) REFERENCES case_type (type)
);
