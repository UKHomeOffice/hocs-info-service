DROP TABLE IF EXISTS house_address;

CREATE TABLE IF NOT EXISTS house_address
(
  id         BIGSERIAL PRIMARY KEY,
  uuid       UUID NOT NULL,
  house      TEXT NOT NULL,
  house_code TEXT NOT NULL,
  address1   TEXT NOT NULL,
  address2   TEXT NOT NULL,
  address3   TEXT,
  postcode   TEXT NOT NULL,
  country    TEXT,
  added      DATE NOT NULL,
  updated    DATE,


  CONSTRAINT house_address_uuid_idempotent UNIQUE (uuid),
  CONSTRAINT house_address_house_house_address_uuid_idempotent UNIQUE (house)

);

DROP TABLE IF EXISTS member;

CREATE TABLE IF NOT EXISTS member
(
  id                 BIGSERIAL PRIMARY KEY,
  full_title         TEXT                  NOT NULL,
  external_reference TEXT                  NOT NULL,
  house              TEXT                  NOT NULL,
  uuid               UUID                  NOT NULL,
  house_address_uuid UUID                  NOT NULL,
  updated            DATE                  NOT NULL,
  deleted            BOOLEAN DEFAULT FALSE NOT NULL,

  CONSTRAINT member_name_ref_idempotent UNIQUE (full_title, external_reference, house),
  CONSTRAINT member_uuid_idempotent UNIQUE (uuid),
  CONSTRAINT fk_house_address_id FOREIGN KEY (house_address_uuid) REFERENCES house_address (uuid)

);

CREATE INDEX idx_member_active
  ON member (deleted);


DROP TABLE IF EXISTS member_team;

CREATE TABLE IF NOT EXISTS member_team
(
  id          BIGSERIAL PRIMARY KEY,
  member_UUID UUID NOT NULL,
  tenant_role TEXT NOT NULL,
  team_uuid   UUID NOT NULL,

  --   CONSTRAINT fk_member_team_member_id FOREIGN KEY (member_uuid) REFERENCES member (uuid),
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
