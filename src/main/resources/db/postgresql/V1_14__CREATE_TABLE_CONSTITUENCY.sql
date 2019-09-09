ALTER TABLE member
ADD COLUMN IF NOT EXISTS constituency_uuid UUID;

ALTER TABLE member
ADD COLUMN IF NOT EXISTS constituency_name TEXT;


DROP TABLE IF EXISTS region;

CREATE TABLE IF NOT EXISTS region
(
  id                 BIGSERIAL  PRIMARY KEY,
  uuid               UUID       NOT NULL,
  region_name        TEXT       NOT NULL,
  active             BOOLEAN    NOT NULL DEFAULT TRUE,

  CONSTRAINT region_uuid_unique UNIQUE (uuid),
  CONSTRAINT region_name_unique UNIQUE (region_name)
);

CREATE INDEX idx_region_uuid
  ON region (uuid);

CREATE INDEX idx_region_name
  ON region (region_name, active);


DROP TABLE IF EXISTS constituency;

CREATE TABLE IF NOT EXISTS constituency
(
  id                 BIGSERIAL  PRIMARY KEY,
  uuid               UUID       NOT NULL,
  constituency_name  TEXT       NOT NULL,
  region_uuid        UUID       NULL,
  active             BOOLEAN    NOT NULL DEFAULT TRUE,

  CONSTRAINT constituency_uuid_unique UNIQUE (uuid),
  CONSTRAINT fk_constituency_region_uuid FOREIGN KEY (region_uuid) REFERENCES region (uuid)
);

CREATE INDEX idx_constituency_uuid
  ON constituency (uuid);

CREATE INDEX idx_constituency_name_active
  ON constituency (constituency_name, active);
