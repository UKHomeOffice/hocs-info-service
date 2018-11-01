DROP TABLE IF EXISTS standard_line;

CREATE TABLE IF NOT EXISTS standard_line
(
  id           BIGSERIAL PRIMARY KEY,
  uuid         UUID NOT NULL,
  display_name TEXT NOT NULL,
  topic_uuid   UUID NOT NULL,
  expires      TIMESTAMP NOT NULL,

  CONSTRAINT standard_line_uuid_idempotent UNIQUE (uuid),
  CONSTRAINT standard_line_name_idempotent UNIQUE (display_name, uuid)
);

CREATE INDEX idx_standard_line_uuid
  ON standard_line (uuid);
CREATE INDEX idx_standard_line_all
  ON standard_line (display_name, uuid);

DROP TABLE IF EXISTS template;

CREATE TABLE IF NOT EXISTS template
(
  id           BIGSERIAL PRIMARY KEY,
  display_name TEXT    NOT NULL,
  case_type    TEXT    NOT NULL,
  uuid         UUID    NOT NULL,
  deleted      boolean NOT NULL,

  CONSTRAINT template_uuid_idempotent UNIQUE (uuid),
  CONSTRAINT template_name_idempotent UNIQUE (display_name, uuid),
  CONSTRAINT fk_template_id FOREIGN KEY (case_type) REFERENCES case_type (type)
);

CREATE INDEX idx_template_uuid
  ON template (uuid);
CREATE INDEX idx_template_all
  ON template (display_name, uuid);