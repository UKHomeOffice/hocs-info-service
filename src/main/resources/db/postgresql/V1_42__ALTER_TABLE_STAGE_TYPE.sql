--HOCS-1848
UPDATE stage_type SET display_stage_order = 50 WHERE display_stage_order IS NULL;

ALTER TABLE stage_type ALTER COLUMN display_stage_order SET NOT NULL;
