
ALTER TABLE info.stage_type
    ADD COLUMN can_display_stage BOOLEAN NOT NULL DEFAULT true,
    ADD COLUMN display_stage_order SMALLINT DEFAULT NULL;

COMMENT ON COLUMN info.stage_type.can_display_stage is 'Boolean to indicate whether the stage should be displayed on the "view all stages" screen';
COMMENT ON COLUMN info.stage_type.display_stage_order is 'Determines the display order (top to bottom) on the "view all stages" screen';
