DROP TABLE IF EXISTS house_address;

CREATE TABLE IF NOT EXISTS house_address
(
  id           BIGSERIAL PRIMARY KEY,
  uuid         UUID    NOT NULL,
  house        TEXT    NOT NULL,
  address1     TEXT    NOT NULL,
  address2     TEXT    NOT NULL,
  address3     TEXT,
  postcode     TEXT    NOT NULL,
  country      TEXT,
  added        DATE    NOT NULL,
  updated      DATE,



    CONSTRAINT house_address_uuid_idempotent UNIQUE (uuid),
    CONSTRAINT house_address_house_house_address_uuid_idempotent UNIQUE (house)
);
