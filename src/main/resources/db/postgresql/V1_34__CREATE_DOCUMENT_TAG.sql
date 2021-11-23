
DROP TABLE IF EXISTS document_tag;

CREATE TABLE IF NOT EXISTS document_tag
(
    id                      BIGSERIAL   PRIMARY KEY,
    uuid                    UUID        NOT NULL,
    case_type_uuid          UUID        NOT NULL,
    tag                     TEXT        NOT NULL,
    sort_order              SMALLINT    NOT NULL,

    CONSTRAINT uq_document_tag_uuid UNIQUE (uuid),
    CONSTRAINT fk_document_tag_case_type_uuid FOREIGN KEY (case_type_uuid) REFERENCES case_type (uuid)
);
