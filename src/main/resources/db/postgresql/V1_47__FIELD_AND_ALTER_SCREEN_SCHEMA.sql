ALTER TABLE info.field
    ADD COLUMN child_field_id bigint;

ALTER TABLE
    info.screen_schema ADD validation JSONB DEFAULT '{}';
