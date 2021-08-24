ALTER TABLE entity
    ADD COLUMN sort_order INTEGER;

-- Gives us some leeway between the values for additions and deletions
UPDATE entity
SET sort_order = id * 10;

-- Make column not null after amending existing values
ALTER TABLE entity
    ALTER COLUMN sort_order SET NOT NULL;

-- Make a unique constraint between the entity_list_uuid and sort order to stop
-- 2 items having the same sort_order
ALTER TABLE entity
    ADD CONSTRAINT entity_uuid_sort_order_unique UNIQUE (entity_list_uuid, sort_order);
