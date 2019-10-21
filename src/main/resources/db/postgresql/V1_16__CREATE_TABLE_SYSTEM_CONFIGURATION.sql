DROP TABLE IF EXISTS system_configuration;

CREATE TABLE IF NOT EXISTS system_configuration
(
  system_name  VARCHAR(25) PRIMARY KEY,
  display_name TEXT NOT NULL,
  document_labels TEXT,
  workstack_columns TEXT

);

INSERT INTO  system_configuration (system_name, display_name, document_labels, workstack_columns)
VALUES ('system', 'Correspondence System','ORIGINAL,DRAFT,FINAL', 'Select,Reference,CurrentStage,Owner,Team,Deadline');


