ALTER TABLE screen_schema
    ADD COLUMN summary JSONB NULL;

COMMENT ON COLUMN screen_schema.summary IS 'JSONB array of objects containing label, case data lookup and optional renderer.'
