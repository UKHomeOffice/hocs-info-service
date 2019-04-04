DROP TABLE IF EXISTS case_type_schema;

CREATE TABLE IF NOT EXISTS case_type_schema
(
       id          BIGSERIAL PRIMARY KEY,
       case_type   TEXT NOT NULL,
       stage_type  TEXT NOT NULL,
       schema_uuid   UUID    NOT NULL,

       CONSTRAINT fk_case_type_schema_case_type FOREIGN KEY (case_type) REFERENCES case_type (type),
       CONSTRAINT fk_case_type_schema_stage_type FOREIGN KEY (stage_type) REFERENCES stage_type (type),
       CONSTRAINT fk_case_type_schema_uuid FOREIGN KEY (schema_uuid) REFERENCES screen_schema (uuid)

);

INSERT INTO case_type_schema (schema_uuid, case_type, stage_type)
VALUES ('afa670fa-8048-4207-a0f6-35e856ffb70d', 'MIN', 'DCU_MIN_DATA_INPUT'),
       ('afa670fa-8048-4207-a0f6-35e856ffb70d', 'TRO', 'DCU_TRO_DATA_INPUT'),
       ('4b9aa734-8048-4f77-9034-e0d9efaa2f77', 'DTEN','DCU_DTEN_DATA_INPUT'),
       ('acbec747-a86c-4812-877f-633e049aedc2', 'MIN', 'DCU_MIN_DATA_INPUT'),
       ('acbec747-a86c-4812-877f-633e049aedc2', 'TRO', 'DCU_TRO_DATA_INPUT'),
       ('acbec747-a86c-4812-877f-633e049aedc2', 'DTEN','DCU_DTEN_DATA_INPUT'),
       ('38740712-9873-4ece-ac60-e8f4f9f3ec53', 'MIN', 'DCU_MIN_DATA_INPUT'),
       ('38740712-9873-4ece-ac60-e8f4f9f3ec53', 'TRO', 'DCU_TRO_DATA_INPUT'),
       ('38740712-9873-4ece-ac60-e8f4f9f3ec53', 'DTEN','DCU_DTEN_DATA_INPUT'),
       ('56c6ee9f-216d-42da-910e-df68fe56276c', 'MIN', 'DCU_MIN_DATA_INPUT'),
       ('56c6ee9f-216d-42da-910e-df68fe56276c', 'TRO', 'DCU_TRO_DATA_INPUT'),
       ('56c6ee9f-216d-42da-910e-df68fe56276c', 'DTEN','DCU_DTEN_DATA_INPUT'),
       ('a47bb3a8-e9c2-4e84-b3e2-458dd95d8139', 'MIN', 'DCU_MIN_DATA_INPUT'),
       ('a47bb3a8-e9c2-4e84-b3e2-458dd95d8139', 'TRO', 'DCU_TRO_DATA_INPUT'),
       ('a47bb3a8-e9c2-4e84-b3e2-458dd95d8139', 'DTEN','DCU_DTEN_DATA_INPUT'),
       ('03341d10-55c6-41c3-936d-519ebe9ee762', 'MIN', 'DCU_MIN_DATA_INPUT'),
       ('03341d10-55c6-41c3-936d-519ebe9ee762', 'TRO', 'DCU_TRO_DATA_INPUT'),
       ('03341d10-55c6-41c3-936d-519ebe9ee762', 'DTEN','DCU_DTEN_DATA_INPUT'),
       ('07257739-4796-45a5-a462-1d3ecb516a32', 'MIN', 'DCU_MIN_DATA_INPUT'),
       ('07257739-4796-45a5-a462-1d3ecb516a32', 'TRO', 'DCU_TRO_DATA_INPUT'),
       ('07257739-4796-45a5-a462-1d3ecb516a32', 'DTEN','DCU_DTEN_DATA_INPUT'),

       ('d1119106-b5ef-4db1-9f77-5fc8be01333d', 'MIN', 'DCU_MIN_TRANSFER_CONFIRMATION'),
       ('d1119106-b5ef-4db1-9f77-5fc8be01333d', 'TRO', 'DCU_TRO_TRANSFER_CONFIRMATION'),
       ('d1119106-b5ef-4db1-9f77-5fc8be01333d', 'DTEN','DCU_DTEN_TRANSFER_CONFIRMATION'),

       ('faf134ae-a3f8-41bd-9aaf-52c7393d1a52', 'MIN', 'DCU_MIN_NO_REPLY_NEEDED_CONFIRMATION'),
       ('faf134ae-a3f8-41bd-9aaf-52c7393d1a52', 'TRO', 'DCU_TRO_NO_REPLY_NEEDED_CONFIRMATION'),
       ('faf134ae-a3f8-41bd-9aaf-52c7393d1a52', 'DTEN','DCU_DTEN_NO_REPLY_NEEDED_CONFIRMATION'),

       ('241780f4-0fcc-4297-8ad0-e60230ba84d1', 'MIN', 'DCU_MIN_INITIAL_DRAFT'),
       ('241780f4-0fcc-4297-8ad0-e60230ba84d1', 'TRO', 'DCU_TRO_INITIAL_DRAFT'),
       ('241780f4-0fcc-4297-8ad0-e60230ba84d1', 'DTEN','DCU_DTEN_INITIAL_DRAFT'),
       ('3c3b0dc0-ad8a-4be1-a377-aa20fbd67f20', 'MIN', 'DCU_MIN_INITIAL_DRAFT'),
       ('3c3b0dc0-ad8a-4be1-a377-aa20fbd67f20', 'TRO', 'DCU_TRO_INITIAL_DRAFT'),
       ('3c3b0dc0-ad8a-4be1-a377-aa20fbd67f20', 'DTEN','DCU_DTEN_INITIAL_DRAFT'),
       ('63518bde-0bd0-4389-8eeb-4ea6744f4193', 'MIN', 'DCU_MIN_INITIAL_DRAFT'),
       ('63518bde-0bd0-4389-8eeb-4ea6744f4193', 'TRO', 'DCU_TRO_INITIAL_DRAFT'),
       ('63518bde-0bd0-4389-8eeb-4ea6744f4193', 'DTEN','DCU_DTEN_INITIAL_DRAFT'),
       ('0dd19543-a0f7-411e-a9bf-568e90a1db91', 'MIN', 'DCU_MIN_INITIAL_DRAFT'),
       ('0dd19543-a0f7-411e-a9bf-568e90a1db91', 'TRO', 'DCU_TRO_INITIAL_DRAFT'),
       ('df60288f-1eda-4420-aa61-d819f2697293', 'MIN', 'DCU_MIN_INITIAL_DRAFT'),
       ('df60288f-1eda-4420-aa61-d819f2697293', 'TRO', 'DCU_TRO_INITIAL_DRAFT'),
       ('df60288f-1eda-4420-aa61-d819f2697293', 'DTEN','DCU_DTEN_INITIAL_DRAFT'),
       ('5e0695b1-b0d4-43cc-915c-c67a1da0cf76', 'MIN', 'DCU_MIN_INITIAL_DRAFT'),
       ('5e0695b1-b0d4-43cc-915c-c67a1da0cf76', 'DTEN','DCU_DTEN_INITIAL_DRAFT'),
       ('f26ee9cc-a9b7-460d-bd66-898a90ed0ee6', 'MIN', 'DCU_MIN_INITIAL_DRAFT'),
       ('f26ee9cc-a9b7-460d-bd66-898a90ed0ee6', 'DTEN','DCU_DTEN_INITIAL_DRAFT'),
       
       ('8f1d3c22-9aea-4319-9357-0f485dfc2223', 'MIN', 'DCU_MIN_QA_RESPONSE'),
       ('8f1d3c22-9aea-4319-9357-0f485dfc2223', 'TRO', 'DCU_TRO_QA_RESPONSE'),
       ('8f1d3c22-9aea-4319-9357-0f485dfc2223', 'DTEN','DCU_DTEN_QA_RESPONSE'),
       ('0fe10690-02cb-4264-b4c9-c090f24d47c2', 'MIN', 'DCU_MIN_QA_RESPONSE'),
       ('0fe10690-02cb-4264-b4c9-c090f24d47c2', 'TRO', 'DCU_TRO_QA_RESPONSE'),
       ('0fe10690-02cb-4264-b4c9-c090f24d47c2', 'DTEN','DCU_DTEN_QA_RESPONSE'),

       ('0d18c411-d86e-433f-9a91-6eb0c7e2d129', 'MIN', 'DCU_MIN_PRIVATE_OFFICE'),
       ('0f990142-4856-4564-8eb0-ece85e2c10cf', 'DTEN','DCU_DTEN_PRIVATE_OFFICE'),
       ('f145b5a1-8635-4f3a-b411-065f53e71fa5', 'MIN', 'DCU_MIN_PRIVATE_OFFICE'),
       ('f145b5a1-8635-4f3a-b411-065f53e71fa5', 'DTEN','DCU_DTEN_PRIVATE_OFFICE'),
       ('9aa32d23-3e5a-43e0-b415-0c4db40a9ab0', 'MIN', 'DCU_MIN_PRIVATE_OFFICE'),

       ('6dffa215-2107-432d-a784-e54655af2c56', 'MIN', 'DCU_MIN_MINISTER_SIGN_OFF'),
       ('bdf78e93-2cde-44d0-b5c7-04186c5d71b7', 'MIN', 'DCU_MIN_MINISTER_SIGN_OFF'),

       ('47f0f8a0-71b1-42b4-be3c-1908121c3fb5', 'MIN', 'DCU_MIN_DISPATCH'),
       ('47f0f8a0-71b1-42b4-be3c-1908121c3fb5', 'TRO', 'DCU_TRO_DISPATCH'),
       ('fa52bcc8-93b8-44cb-837f-82e8343f5b0f', 'DTEN', 'DCU_DTEN_DISPATCH'),
       ('1c14536c-ded9-41b8-8b39-e558087970e1', 'MIN', 'DCU_MIN_DISPATCH'),
       ('1c14536c-ded9-41b8-8b39-e558087970e1', 'TRO', 'DCU_TRO_DISPATCH'),  
       ('1c14536c-ded9-41b8-8b39-e558087970e1', 'DTEN', 'DCU_DTEN_DISPATCH'),
       ('2212fd30-f34e-43b8-a904-23e3ffbc885a', 'MIN', 'DCU_MIN_DISPATCH'),
       ('2212fd30-f34e-43b8-a904-23e3ffbc885a', 'TRO', 'DCU_TRO_DISPATCH'),
       ('2212fd30-f34e-43b8-a904-23e3ffbc885a', 'DTEN', 'DCU_DTEN_DISPATCH'),

       ('dedbf417-5604-4c5f-9731-9d4d23599c9b', 'MIN', 'DCU_MIN_COPY_NUMBER_TEN'),
       ('dedbf417-5604-4c5f-9731-9d4d23599c9b', 'TRO', 'DCU_TRO_COPY_NUMBER_TEN')