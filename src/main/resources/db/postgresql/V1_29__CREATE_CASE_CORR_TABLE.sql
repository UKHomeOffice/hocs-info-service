
DROP TABLE IF EXISTS case_type_correspondent_type CASCADE;

ALTER TABLE correspondent_type DROP CONSTRAINT IF EXISTS uuid_unique;


ALTER TABLE correspondent_type ADD CONSTRAINT uuid_unique UNIQUE (uuid);

CREATE TABLE IF NOT EXISTS case_type_correspondent_type
(
    id                      BIGSERIAL   PRIMARY KEY,
    case_type_uuid          UUID        NOT NULL,
    correspondent_type_uuid UUID        NOT NULL,

    CONSTRAINT uq_case_type_uuid_correspondent_type_uuid UNIQUE (case_type_uuid,correspondent_type_uuid),
    CONSTRAINT fk_case_type_correspondent_type_case_type_uuid FOREIGN KEY (case_type_uuid) REFERENCES case_type (uuid),
    CONSTRAINT fk_case_type_correspondent_type_correspondent_type_uuid FOREIGN KEY (correspondent_type_uuid) REFERENCES correspondent_type (uuid)
);
