-- Alter case type table to add new column for initial case
-- Initial case is a case created by a user via Create Case, no previous case reference linked to it.

ALTER TABLE case_type
ADD COLUMN initial_case
BOOLEAN
DEFAULT TRUE;
