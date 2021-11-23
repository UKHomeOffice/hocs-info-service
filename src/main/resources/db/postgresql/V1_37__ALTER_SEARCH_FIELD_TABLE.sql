ALTER TABLE search_field
RENAME COLUMN parent_system_name TO profile_name;

ALTER TABLE search_field
DROP CONSTRAINT fk_search_field_system_name;

