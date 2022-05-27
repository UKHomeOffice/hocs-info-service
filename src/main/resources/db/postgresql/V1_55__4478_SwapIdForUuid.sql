-- Add child_field column to field
ALTER TABLE field
    ADD COLUMN child_field uuid
        CONSTRAINT fk_field_child_field_uuid REFERENCES field(uuid);

UPDATE field
SET child_field =
    (SELECT uuid FROM field f
    WHERE f.id = field.child_field_id);

ALTER TABLE field
    DROP COLUMN child_field_id;
