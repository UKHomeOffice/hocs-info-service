SET search_path TO info;

DROP TABLE IF EXISTS case_action_type;

CREATE OR REPLACE FUNCTION update_on_data_change()
    RETURNS TRIGGER AS $$
BEGIN
    IF (tg_op = 'INSERT') OR row(NEW.*) IS DISTINCT FROM row(OLD.*) THEN
        NEW.last_updated_timestamp = now();
        RETURN NEW;
    ELSE
        RETURN OLD;
    END IF;
END;
$$ language 'plpgsql';

CREATE TABLE case_action_type
(
    id                          BIGSERIAL PRIMARY KEY,
    uuid                        UUID NOT NULL,
    case_type_uuid              UUID NOT NULL,
    case_type_type              TEXT NOT NULL,
    action_type                 TEXT NOT NULL,
    supplementary_data          jsonb,
    sort_order                  INTEGER,
    active                      BOOLEAN DEFAULT FALSE,
    created_timestamp           TIMESTAMP WITH TIME ZONE DEFAULT now(),
    last_updated_timestamp      TIMESTAMP WITH TIME ZONE

);

CREATE TRIGGER update_case_action_type_last_updated_timestamp
    BEFORE INSERT OR UPDATE ON case_action_type FOR EACH ROW EXECUTE PROCEDURE update_on_data_change();
