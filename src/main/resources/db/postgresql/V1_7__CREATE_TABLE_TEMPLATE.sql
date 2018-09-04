DROP TABLE IF EXISTS standard_line;

CREATE TABLE IF NOT EXISTS standard_line
(
  id           BIGSERIAL PRIMARY KEY,
  display_name TEXT NOT NULL,
  document_key TEXT NOT NULL,
  uuid         UUID NOT NULL,

  CONSTRAINT standard_line_uuid_idempotent UNIQUE (uuid),
  CONSTRAINT standard_line_name_idempotent UNIQUE (display_name),
  CONSTRAINT standard_line_document_key_idempotent UNIQUE (document_key)
);

CREATE INDEX idx_standard_line_uuid
  ON standard_line (uuid);
CREATE INDEX idx_standard_line_all
  ON standard_line (display_name, document_key, uuid);

DROP TABLE IF EXISTS standard_line_topic;

CREATE TABLE IF NOT EXISTS standard_line_topic
(
  id                 BIGSERIAL PRIMARY KEY,
  standard_line_UUID UUID    NOT NULL,
  tenant_role        TEXT    NOT NULL,
  topic_uuid         UUID    NOT NULL,
  active             boolean NOT NULL,

  CONSTRAINT fk_standard_line_team_member_id FOREIGN KEY (standard_line_UUID) REFERENCES standard_line (uuid),
  CONSTRAINT fk_standard_line_tenant_role FOREIGN KEY (tenant_role) REFERENCES tenant (role),
  CONSTRAINT fk_standard_line_team_team_id FOREIGN KEY (topic_uuid) REFERENCES topic (uuid)

);

CREATE INDEX idx_standard_line_active
  ON standard_line_topic (standard_line_UUID, tenant_role, topic_uuid, active);

DROP TABLE IF EXISTS template;

CREATE TABLE IF NOT EXISTS template
(
  id           BIGSERIAL PRIMARY KEY,
  display_name TEXT    NOT NULL,
  case_type    TEXT    NOT NULL,
  document_key TEXT    NOT NULL,
  uuid         UUID    NOT NULL,
  active       boolean NOT NULL,

  CONSTRAINT template_uuid_idempotent UNIQUE (uuid),
  CONSTRAINT template_name_idempotent UNIQUE (display_name),
  CONSTRAINT template_document_key_idempotent UNIQUE (document_key),
  CONSTRAINT fk_template_id FOREIGN KEY (case_type) REFERENCES case_type (type)
);

CREATE INDEX idx_template_uuid
  ON template (uuid);
CREATE INDEX idx_template_all
  ON template (display_name, document_key, uuid);