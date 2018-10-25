
ALTER TABLE standard_line_topic RENAME COLUMN active TO deleted;
UPDATE standard_line_topic SET deleted = FALSE;


ALTER TABLE standard_line ADD COLUMN expires DATE;
UPDATE standard_line SET expires = '2020-12-31';
ALTER TABLE standard_line ALTER COLUMN expires SET NOT NULL;


