DROP TABLE IF EXISTS case_type_profile;

CREATE TABLE IF NOT EXISTS case_type_profile
(
    case_type     TEXT NOT NULL,
    profile_name  VARCHAR(25)  NOT NULL,

    CONSTRAINT case_type_profile_idempotent UNIQUE (case_type, profile_name),
    CONSTRAINT fk_case_type_profile_ctype FOREIGN KEY (case_type) REFERENCES case_type (type),
    CONSTRAINT fk_case_type_profile_pname FOREIGN KEY (profile_name) REFERENCES profile (profile_name)
);

