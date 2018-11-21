DROP TABLE IF EXISTS case_type;

CREATE TABLE IF NOT EXISTS case_type
(
  id           BIGSERIAL PRIMARY KEY,
  display_name TEXT    NOT NULL,
  type         TEXT    NOT NULL,
  tenant_role  TEXT    NOT NULL,
  active       boolean NOT NULL,
  bulk         boolean NULL,

    CONSTRAINT case_type_type_idempotent UNIQUE ( type ),
  CONSTRAINT case_type_name_idempotent UNIQUE (display_name)
);

CREATE INDEX idx_case_type_tenant_role
  ON case_type (tenant_role);

CREATE INDEX idx_case_type_active
  ON case_type (active);

DROP TABLE IF EXISTS sla;

CREATE TABLE IF NOT EXISTS sla
(
  id         BIGSERIAL PRIMARY KEY,
  stage_type TEXT NOT NULL,
  value      int  NOT NULL,
  case_type  TEXT NOT NULL,

  CONSTRAINT sla_sla_idempotent UNIQUE (stage_type, case_type),
  CONSTRAINT fk_sla_id FOREIGN KEY (case_type) REFERENCES case_type (type)
);

CREATE INDEX idx_sla_case_type
  ON sla (case_type);

DROP TABLE IF EXISTS holiday_date;

CREATE TABLE IF NOT EXISTS holiday_date
(
  id        BIGSERIAL PRIMARY KEY,
  date      DATE NOT NULL,
  case_type TEXT NOT NULL,
  CONSTRAINT holiday_date_sla_idempotent UNIQUE (date, case_type),
  CONSTRAINT fk_holiday_id FOREIGN KEY (case_type) REFERENCES case_type (type)
);

CREATE INDEX idx_holiday_data_case_type
  ON holiday_date (case_type);