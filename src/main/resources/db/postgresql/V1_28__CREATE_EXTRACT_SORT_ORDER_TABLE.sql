DROP TABLE IF EXISTS extract_field_sort_order;

CREATE TABLE IF NOT EXISTS extract_field_sort_order
(
    id            BIGSERIAL PRIMARY KEY,
    case_type   TEXT      NOT NULL,
    field_name  TEXT      NOT NULL,
    sort_order BIGSERIAL NOT NULL,
    CONSTRAINT ext_field_sort_case_field_idempotent UNIQUE (case_type, field_name)
);