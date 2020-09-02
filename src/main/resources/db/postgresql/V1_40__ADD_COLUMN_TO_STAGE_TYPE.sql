
ALTER TABLE stage_type
    ADD COLUMN deadline_warning INT DEFAULT -2 NOT NULL;

COMMENT ON COLUMN stage_type.deadline_warning is 'Number of days after which deadline warning should be displayed, -2 for case default.';
