DROP TABLE IF EXISTS workstack_column;

CREATE TABLE IF NOT EXISTS workstack_column
(
  id     BIGSERIAL PRIMARY KEY,
  parent_system_name  VARCHAR(25) NOT NULL,
  display_name        TEXT NOT NULL,
  data_adapter        TEXT,
  renderer            TEXT,
  data_value_key      TEXT NOT NULL,
  is_filterable       BOOLEAN NOT NULL,
  header_class_name   TEXT NOT NULL,

  CONSTRAINT workstack_column_name_system_name_idempotent UNIQUE (display_name, parent_system_name),
  CONSTRAINT fk_workstack_column_system_name FOREIGN KEY (parent_system_name) REFERENCES system_configuration (system_name)
);

INSERT INTO  workstack_column (parent_system_name, display_name, data_adapter, renderer, data_value_key, is_filterable, header_class_name)
VALUES ('system', 'Reference', null, 'caseLink', 'caseReference', true, 'govuk-table__header'),
       ('system', 'Current Stage', null, null, 'stageTypeDisplay', true, 'govuk-table__header'),
       ('system', 'Owner', null, null, 'assignedUserDisplay', true, 'govuk-table__header govuk-!-width-one-quarter'),
       ('system', 'Team', null, null, 'assignedTeamDisplay', true, 'govuk-table__header govuk-!-width-one-quarter'),
       ('system', 'Deadline', null, null, 'deadlineDisplay', true, 'govuk-table__header');

CREATE INDEX idx_workstack_column_name_system_name
  ON workstack_column(display_name, parent_system_name);
