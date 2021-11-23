DROP TABLE IF EXISTS profile;

CREATE TABLE IF NOT EXISTS profile
(
    profile_name            VARCHAR(25)  PRIMARY KEY,
    parent_system_name      VARCHAR(25)  NOT NULL,

    CONSTRAINT fk_profile_parent_system_name FOREIGN KEY (parent_system_name) REFERENCES system_configuration (system_name)
);

