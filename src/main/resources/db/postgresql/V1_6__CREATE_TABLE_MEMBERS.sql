DROP TABLE IF EXISTS member;

DROP TABLE IF EXISTS house;

CREATE TABLE IF NOT EXISTS house
(
  id           BIGSERIAL PRIMARY KEY,
  display_name TEXT    NOT NULL,
  type         TEXT    NOT NULL,

  CONSTRAINT house_idempotent UNIQUE (type)
);

Create INDEX idx_house_house
  ON house(type);

CREATE TABLE IF NOT EXISTS member
(
  id           BIGSERIAL PRIMARY KEY,
  display_name TEXT    NOT NULL,
  letter_name  TEXT    ,
  external_id  TEXT    NOT NULL,
  house  TEXT    NOT NULL,
  uuid         UUID    NOT NULL,
  active       boolean NOT NULL,

  CONSTRAINT member_external_id_idempotent UNIQUE (external_id, house),
  CONSTRAINT member_uuid_idempotent UNIQUE (uuid),
  CONSTRAINT member_name_idempotent UNIQUE (display_name),
  CONSTRAINT fk_member_role FOREIGN KEY (house) REFERENCES house (type)
);

CREATE INDEX idx_member_house
  ON member (house);

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

DROP TABLE IF EXISTS house_case_type;

CREATE TABLE IF NOT EXISTS house_case_type
(
  id          BIGSERIAL PRIMARY KEY,
  house TEXT NOT NULL,
  case_type   TEXT NOT NULL,

  CONSTRAINT house_house_idempotent UNIQUE (house, case_type),
  CONSTRAINT fk_house_tenant_house FOREIGN KEY (house) REFERENCES house (type),
  CONSTRAINT fk_house_case_type FOREIGN KEY (case_type) REFERENCES case_type (type)
);

CREATE INDEX idx_house_case_type_house
  ON house_case_type (house) ;

CREATE INDEX idx_house_case_type_case_type
  ON house_case_type (case_type);
