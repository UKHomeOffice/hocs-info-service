-- Add flag to units indicating whether to allow case transfers between teams within
ALTER TABLE unit
    ADD COLUMN allow_bulk_team_transfer BOOLEAN DEFAULT FALSE;
