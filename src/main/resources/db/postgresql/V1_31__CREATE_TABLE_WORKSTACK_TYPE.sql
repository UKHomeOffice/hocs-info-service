-- Remove parent_system_name column as system_configuration relationship is now via the
-- defined below
ALTER TABLE info.workstack_column
    DROP COLUMN parent_system_name,
    ADD CONSTRAINT workstack_column_display_name_data_value_key_idempotent UNIQUE (display_name, data_value_key);

DROP TABLE IF EXISTS info.workstack_type;
CREATE TABLE info.workstack_type
(
    id                    BIGSERIAL PRIMARY KEY,
    parent_system_name    VARCHAR(25) NOT NULL DEFAULT 'system',
    type                  VARCHAR(50) NOT NULL,

    CONSTRAINT workstack_type_type_idempotent UNIQUE (type),
    CONSTRAINT fk_workstack_type_parent_system_name FOREIGN KEY (parent_system_name)
        REFERENCES info.system_configuration (system_name)
);

INSERT INTO info.workstack_type (type)
VALUES ('DEFAULT'),
       ('DTEN'),
       ('MIN'),
       ('TRO'),
       ('UKVI');

DROP TABLE IF EXISTS info.workstack_column_type;
CREATE TABLE info.workstack_column_type
(
    workstack_column_id      BIGSERIAL NOT NULL,
    workstack_type_type      VARCHAR(50) NOT NULL,
    workstack_column_order   SMALLINT NOT NULL,

    CONSTRAINT fk_workstack_column_id_workstack_column_id FOREIGN KEY (workstack_column_id)
        REFERENCES info.workstack_column(id),

    CONSTRAINT fk_workstack_type_type_workstack_type_type FOREIGN KEY (workstack_type_type)
        REFERENCES info.workstack_type(type),

    CONSTRAINT workstack_column_id_workstack_type_type_idempotent UNIQUE (workstack_column_id, workstack_type_type),

    CONSTRAINT workstack_type_type_workstack_column_order_idempotent UNIQUE (workstack_type_type, workstack_column_order)
);
