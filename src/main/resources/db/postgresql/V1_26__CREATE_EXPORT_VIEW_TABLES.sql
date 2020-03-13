DROP TABLE IF EXISTS export_view;

CREATE TABLE IF NOT EXISTS export_view
(
    id                       BIGSERIAL PRIMARY KEY,
    code                     TEXT NOT NULL,
    display_name              TEXT NOT NULL,
    required_permission      TEXT,
    CONSTRAINT export_view_idempotent UNIQUE (code)
);

DROP TABLE IF EXISTS export_view_field;

CREATE TABLE IF NOT EXISTS export_view_field
(
    id                       BIGSERIAL PRIMARY KEY,
    parent_export_view_id    BIGSERIAL NOT NULL,
    sort_order               BIGSERIAL NOT NULL,
    display_name             TEXT NOT NULL,

    CONSTRAINT fk_export_view_field_parent_view FOREIGN KEY (parent_export_view_id) REFERENCES export_view (id)

);

DROP TABLE IF EXISTS export_view_field_adapter;

CREATE TABLE IF NOT EXISTS export_view_field_adapter
(
    id                              BIGSERIAL PRIMARY KEY,
    parent_export_view_field_id     BIGSERIAL NOT NULL,
    sort_order                      BIGSERIAL NOT NULL,
    type                            TEXT NOT NULL,

    CONSTRAINT fk_export_view_field_adapter_parent_view_field FOREIGN KEY (parent_export_view_field_id) REFERENCES export_view_field (id)

);

