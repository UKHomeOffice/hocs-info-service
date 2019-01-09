DROP TABLE IF EXISTS form;

CREATE TABLE IF NOT EXISTS form
(
  id     BIGSERIAL PRIMARY KEY,
  uuid   UUID    NOT NULL,
  type   TEXT    NOT NULL,
  data   JSONB   NOT NULL DEFAULT '{}',
  active BOOLEAN NOT NULL DEFAULT TRUE,

  CONSTRAINT form_uuid_idempotent UNIQUE (uuid),
  CONSTRAINT form_type_idempotent UNIQUE (type)
);


DROP TABLE IF EXISTS stage_type_form;

CREATE TABLE IF NOT EXISTS stage_type_form
(
  id              BIGSERIAL PRIMARY KEY,
  stage_type_uuid UUID NOT NULL,
  form_uuid       UUID NOT NULL,

  CONSTRAINT fk_form_stage_type FOREIGN KEY (stage_type_uuid) REFERENCES stage_type (uuid),
  CONSTRAINT fk_form_form FOREIGN KEY (stage_type_uuid) REFERENCES form (uuid)
);

DROP TABLE IF EXISTS form_case_type;

CREATE TABLE IF NOT EXISTS form_case_type
(
  id          BIGSERIAL PRIMARY KEY,
  form_uuid   UUID    NOT NULL,
  case_type TEXT NOT NULL,

  CONSTRAINT fk_form_case_type_case_type FOREIGN KEY (case_type) REFERENCES case_type (type),
  CONSTRAINT fk_form_case_type_uuid FOREIGN KEY (form_uuid) REFERENCES form (uuid)

);

CREATE INDEX idx_form_case_type_case_type
  ON form_case_type(case_type);


DROP TABLE IF EXISTS field;

CREATE TABLE IF NOT EXISTS field
(
  id     BIGSERIAL PRIMARY KEY,
  uuid   UUID    NOT NULL,
  form_uuid   UUID    NOT NULL,
  data   JSONB   NOT NULL DEFAULT '{}',
  summary BOOLEAN NOT NULL DEFAULT FALSE,
  active BOOLEAN NOT NULL DEFAULT TRUE,

  CONSTRAINT field_uuid_idempotent UNIQUE (uuid),
  CONSTRAINT fk_field_case_type_uuid FOREIGN KEY (form_uuid) REFERENCES form (uuid)
);
