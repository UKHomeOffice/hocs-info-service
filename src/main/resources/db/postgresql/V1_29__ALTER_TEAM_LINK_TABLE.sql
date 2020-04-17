
DO $$
BEGIN
ALTER TABLE team_link ADD COLUMN IF NOT EXISTS link_value VARCHAR(80);

IF (SELECT EXISTS (SELECT 1
    FROM information_schema.columns
    WHERE table_schema='info' AND table_name='team_link' AND column_name='link_uuid')) THEN

    UPDATE team_link
        SET link_value = link_uuid
        WHERE link_value IS null;
END IF;

ALTER TABLE team_link DROP COLUMN IF EXISTS link_uuid;

END
$$
