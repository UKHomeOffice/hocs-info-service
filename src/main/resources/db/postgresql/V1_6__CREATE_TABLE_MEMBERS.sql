DROP TABLE IF EXISTS member;

CREATE TABLE IF NOT EXISTS member
(
  id           BIGSERIAL PRIMARY KEY,
  display_name TEXT    NOT NULL,
  letter_name  TEXT    ,
  external_id  TEXT    NOT NULL,
  legislature  TEXT    NOT NULL,
  uuid         UUID    NOT NULL,
  active       boolean NOT NULL,

  CONSTRAINT member_external_id_idempotent UNIQUE (external_id, legislature),
  CONSTRAINT member_uuid_idempotent UNIQUE (uuid),
  CONSTRAINT member_name_idempotent UNIQUE (display_name)
);


DROP TABLE IF EXISTS member_team;

CREATE TABLE IF NOT EXISTS member_team
(
  id          BIGSERIAL PRIMARY KEY,
  member_UUID UUID    NOT NULL,
  tenant_role TEXT NOT NULL,
  team_uuid   UUID    NOT NULL,

  CONSTRAINT fk_member_team_member_id FOREIGN KEY (member_uuid) REFERENCES member (uuid),
  CONSTRAINT fk_member_tenant_role FOREIGN KEY (tenant_role) REFERENCES tenant (role),
  CONSTRAINT fk_member_team_team_id FOREIGN KEY (team_uuid) REFERENCES team (uuid)

);

DROP TABLE IF EXISTS legislature_tenant;

CREATE TABLE IF NOT EXISTS legislature_tenant
(
  id          BIGSERIAL PRIMARY KEY,
  legislature UUID    NOT NULL,
  tenant_role TEXT NOT NULL,

  CONSTRAINT fk_legislature_tenant_role FOREIGN KEY (tenant_role) REFERENCES tenant (role)
);
