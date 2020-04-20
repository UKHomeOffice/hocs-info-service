
ALTER TABLE team_link
RENAME COLUMN link_uuid TO link_value;

ALTER TABLE team_link
ALTER COLUMN link_value TYPE VARCHAR(80);
