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
