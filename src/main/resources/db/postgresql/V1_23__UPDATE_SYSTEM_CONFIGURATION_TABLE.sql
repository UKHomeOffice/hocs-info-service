ALTER TABLE system_configuration
ADD COLUMN read_only_case_view_adapter TEXT NOT NULL DEFAULT 'CASE_VIEW_ALL_STAGES';
