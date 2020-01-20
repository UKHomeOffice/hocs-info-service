ALTER TABLE system_configuration
ADD COLUMN bulk_create_enabled BOOLEAN NOT NULL DEFAULT TRUE,
ADD COLUMN deadlines_enabled BOOLEAN NOT NULL DEFAULT TRUE,
DROP COLUMN workstack_columns;
