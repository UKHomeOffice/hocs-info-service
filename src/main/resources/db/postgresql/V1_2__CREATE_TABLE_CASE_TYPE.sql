DROP TABLE IF EXISTS case_type;

CREATE TABLE IF NOT EXISTS case_type
(
  id            BIGSERIAL PRIMARY KEY,
  display_name  TEXT    NOT NULL,
  short_code    TEXT    NOT NULL,
  type          TEXT    NOT NULL,
  tenant_role   TEXT    NOT NULL,
  case_deadline TEXT    NOT NULL,
  active        boolean NOT NULL,
  bulk          boolean NULL,

  CONSTRAINT case_type_type_idempotent UNIQUE (type),
  CONSTRAINT case_type_name_idempotent UNIQUE (display_name),
  CONSTRAINT case_type_short_idempotent UNIQUE (short_code)

);

CREATE INDEX idx_case_type_tenant_role
  ON case_type (tenant_role);

CREATE INDEX idx_case_type_active
  ON case_type (active);

DROP TABLE IF EXISTS stage_type;

CREATE TABLE IF NOT EXISTS stage_type
(
  id           BIGSERIAL PRIMARY KEY,
  display_name TEXT    NOT NULL,
  short_code   TEXT    NOT NULL,
  type         TEXT    NOT NULL,
  tenant_role  TEXT    NOT NULL,
  deadline     INT     NOT NULL,
  active       boolean NOT NULL,

  CONSTRAINT stage_type_type_idempotent UNIQUE ( type ),
  CONSTRAINT stage_type_short_idempotent UNIQUE (short_code)
);

CREATE INDEX idx_stage_type_tenant_role
  ON stage_type (tenant_role);

CREATE INDEX idx_stage_type_active
  ON stage_type (active);

CREATE OR REPLACE VIEW sla AS
  SELECT id as id, type as stage_type, deadline as value, tenant_role as case_type
  FROM stage_type
  WHERE deadline <> 0;

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