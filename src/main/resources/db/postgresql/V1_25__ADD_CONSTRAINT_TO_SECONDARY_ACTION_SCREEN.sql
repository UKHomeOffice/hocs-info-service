ALTER TABLE secondary_action_screen
    ADD CONSTRAINT secondary_action_screen_unique_schema_secondary_action
    UNIQUE (schema_uuid,secondary_action_uuid);
