SET search_path TO info;

DROP TABLE IF EXISTS case_type_action;

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

CREATE TABLE case_type_action
(
    uuid                        UUID PRIMARY KEY NOT NULL,
    case_type_uuid              UUID NOT NULL,
    case_type_type              TEXT NOT NULL,
    action_type                 TEXT NOT NULL,
    action_label                TEXT NOT NULL,
    max_concurrent_events       INTEGER,
    props                       jsonb,
    sort_order                  INTEGER,
    active                      BOOLEAN DEFAULT FALSE,
    created_timestamp           TIMESTAMP WITHOUT TIME ZONE DEFAULT now(),
    last_updated_timestamp      TIMESTAMP WITHOUT TIME ZONE
);

CREATE TRIGGER update_case_type_action_last_updated_timestamp
    BEFORE INSERT OR UPDATE ON case_type_action FOR EACH ROW EXECUTE PROCEDURE update_on_data_change();
