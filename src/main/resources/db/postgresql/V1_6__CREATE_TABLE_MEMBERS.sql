DROP TABLE IF EXISTS member;

DROP TABLE IF EXISTS legislature;

CREATE TABLE IF NOT EXISTS legislature
(
  id           BIGSERIAL PRIMARY KEY,
  display_name TEXT    NOT NULL,
  type         TEXT    NOT NULL,

  CONSTRAINT legislature_idempotent UNIQUE (type)
);

Create INDEX idx_legislature_legislature
  ON legislature(type);

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
  CONSTRAINT member_name_idempotent UNIQUE (display_name),
  CONSTRAINT fk_ember_role FOREIGN KEY (legislature) REFERENCES legislature (type)
);

CREATE INDEX idx_member_legislature
  ON member (legislature);

CREATE INDEX idx_member_active
  ON member (active);


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

DROP TABLE IF EXISTS legislature_case_type;

CREATE TABLE IF NOT EXISTS legislature_case_type
(
  id          BIGSERIAL PRIMARY KEY,
  legislature TEXT NOT NULL,
  case_type   TEXT NOT NULL,

  CONSTRAINT legislature_legislature_idempotent UNIQUE (legislature, case_type),
  CONSTRAINT fk_legislature_tenant_legislature FOREIGN KEY (legislature) REFERENCES legislature (type),
  CONSTRAINT fk_legislature_case_type FOREIGN KEY (case_type) REFERENCES case_type (type)
);

CREATE INDEX idx_legislature_case_type_legislature
  ON legislature_case_type (legislature) ;

CREATE INDEX idx_legislature_case_type_case_type
  ON legislature_case_type (case_type);
