DROP TABLE IF EXISTS search_field;

CREATE TABLE IF NOT EXISTS search_field
(
  id     BIGSERIAL PRIMARY KEY,
  parent_system_name  VARCHAR(25) NOT NULL,
  name      TEXT NOT NULL,
  component TEXT NOT NULL,
  validation JSONB NOT NULL DEFAULT '{}',
  props JSONB DEFAULT '{}',

  CONSTRAINT search_field_name_system_name_idempotent UNIQUE (name, parent_system_name),
  CONSTRAINT fk_search_field_system_name FOREIGN KEY (parent_system_name) REFERENCES system_configuration (system_name)
);

INSERT INTO  search_field (parent_system_name, name, component, validation, props)
VALUES ('system', 'caseTypes','checkbox', '[ ]', '{ "label" : "Case type", "choices" : "CASE_TYPES" }'),
       ('system', 'dateReceivedFrom','date', '[ "isValidDate", "isBeforeToday" ]', '{ "label" : "Received on or after" }'),
       ('system', 'dateReceivedTo','date', '[ "isValidDate", "isBeforeToday" ]', '{ "label" : "Received on or before" }'),
       ('system', 'correspondent', 'text', '[ ]', '{ "label" : "Correspondent" }'),
       ('system', 'topic', 'text', '[ ]', '{ "label" : "Topic" }'),
       ('system', 'signOffMinister', 'dropdown', '[ ]', '{ "label" : "Sign-off Team", "choices" : "PRIVATE_OFFICE_TEAMS" }'),
       ('system', 'caseStatus', 'checkbox', '[ ]', '{ "label" : "Case status", "choices" : [ {  "label" : "Include Active Only", "value" : "active" } ] }');

CREATE INDEX idx_search_field_name_system_name
  ON search_field(name, parent_system_name);
