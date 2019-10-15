ALTER TABLE field_screen
    ADD CONSTRAINT field_screen_unique_schema_field
    UNIQUE (schema_uuid,field_uuid);


ALTER TABLE case_type_schema
    ADD CONSTRAINT case_type_schema_unique_schema_case_stage
    UNIQUE (schema_uuid,case_type,stage_type);

