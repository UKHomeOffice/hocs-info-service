ALTER TABLE field_screen
    ADD CONSTRAINT field_screen_unique_schema_field
    UNIQUE (schema_uuid, field_uuid);
