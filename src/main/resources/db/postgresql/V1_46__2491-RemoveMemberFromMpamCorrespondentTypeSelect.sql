-- add a new column determining whether the frontend should show the case type as an option in the
-- "Record Correspondent Details" dropdown
ALTER TABLE case_type_correspondent_type ADD COLUMN selectable boolean DEFAULT TRUE;