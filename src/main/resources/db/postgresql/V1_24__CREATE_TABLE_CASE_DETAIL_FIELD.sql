DROP TABLE IF EXISTS case_details_field;

CREATE TABLE IF NOT EXISTS case_details_field
(
  id     BIGSERIAL PRIMARY KEY,
  case_type  VARCHAR(25) NOT NULL,
  name      TEXT NOT NULL,
  component TEXT NOT NULL,
  props JSONB DEFAULT '{}',
  CONSTRAINT case_details_field_name_case_type_idempotent UNIQUE (name, case_type),
  CONSTRAINT fk_case_details_field_case_type FOREIGN KEY (case_type) REFERENCES case_type (type)
);

CREATE INDEX idx_case_details_field_name_case_type
  ON case_details_field(name, case_type);
