CREATE TYPE BANK_HOLIDAY_REGION AS ENUM ('UNITED_KINGDOM', 'ENGLAND_AND_WALES', 'SCOTLAND', 'NORTHERN_IRELAND');

CREATE TABLE case_type_bank_holiday_region
(
    id             BIGSERIAL PRIMARY KEY,
    case_type_uuid UUID                NOT NULL,
    region         BANK_HOLIDAY_REGION NOT NULL,

    CONSTRAINT case_type_bank_holiday_region_id_idempotent UNIQUE (id),
    CONSTRAINT case_type_bank_holiday_region_idempotent UNIQUE (case_type_uuid, region)
);
