DROP TABLE IF EXISTS tenant;

CREATE TABLE IF NOT EXISTS tenant
(
  id           BIGSERIAL PRIMARY KEY,
  display_name TEXT    NOT NULL,
  role         TEXT NOT NULL,

  CONSTRAINT tenant_uuid_idempotent UNIQUE (role),
  CONSTRAINT tenant_name_idempotent UNIQUE (display_name)
);