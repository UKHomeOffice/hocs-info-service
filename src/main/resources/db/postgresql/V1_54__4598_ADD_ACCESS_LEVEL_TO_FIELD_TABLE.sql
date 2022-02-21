SET search_path TO info;

ALTER TABLE field
ADD access_level TEXT NOT NULL
DEFAULT 'READ';

