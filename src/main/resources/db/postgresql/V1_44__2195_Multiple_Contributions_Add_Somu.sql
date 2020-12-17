CREATE TABLE IF NOT EXISTS somu_type
(
  id                BIGSERIAL   PRIMARY KEY,
  uuid              UUID        NOT NULL,
  case_type         TEXT        NOT NULL,
  type              VARCHAR(20) NOT NULL,
  schema            JSONB       NOT NULL DEFAULT '{}',
  active            BOOLEAN     NOT NULL DEFAULT TRUE,
  
  CONSTRAINT somu_type_uuid_unique UNIQUE (uuid),
  CONSTRAINT somu_type_type_unique UNIQUE (type),
  CONSTRAINT somu_type_case_type_type_unique UNIQUE (case_type, type)
);

CREATE INDEX IF NOT EXISTS idx_somu_type_uuid ON somu_type(uuid);
CREATE INDEX IF NOT EXISTS idx_somu_type_case_type_type ON somu_type(case_type, type);
