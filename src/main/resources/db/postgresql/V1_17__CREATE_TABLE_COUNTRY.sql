DROP TABLE IF EXISTS country;

CREATE TABLE IF NOT EXISTS country
(
  id                BIGSERIAL PRIMARY KEY,
  name              TEXT                  NOT NULL,
  deleted           BOOLEAN DEFAULT FALSE NOT NULL,

  CONSTRAINT country_name_unique UNIQUE (name)
);

CREATE INDEX idx_country_active
  ON country (deleted);
