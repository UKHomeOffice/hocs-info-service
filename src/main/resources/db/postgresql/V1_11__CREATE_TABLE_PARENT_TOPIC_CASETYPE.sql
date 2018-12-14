DROP TABLE IF EXISTS parent_topic_case_type;

CREATE TABLE IF NOT EXISTS parent_topic_case_type
(
  id                BIGSERIAL PRIMARY KEY,
  parent_topic_uuid      UUID    NOT NULL,
  case_type      TEXT NOT NULL,

  CONSTRAINT fk_parent_topic_case_type FOREIGN KEY (case_type) REFERENCES case_type (type),
  CONSTRAINT fk_parent_topic_unit_uuid FOREIGN KEY (parent_topic_uuid) REFERENCES parent_topic (uuid)

);


