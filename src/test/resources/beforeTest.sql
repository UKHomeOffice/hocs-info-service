Insert INTO tenant (display_name, role)
VALUES ('TEST_TENANT', 'TEST_TENANT'),
       ('TEST2_TENANT', 'TEST2_TENANT');

Insert INTO case_type (display_name, type, tenant_role, active, bulk)
VALUES ('Test Case Type 1', 'CT1', 'CT1', true, true),
       ('Test Case Type 2', 'CT2', 'CT2', true, true);

INSERT INTO unit (display_name, uuid,short_code, active)
VALUES ('UNIT 2', '09221c48-b916-47df-9aa0-a0194f86f6dd', 'UNIT2', TRUE),
       ('UNIT 3', '65996106-91a5-44bf-bc92-a6c2f691f062', 'UNIT3', TRUE);

INSERT INTO team (display_name, uuid, unit_uuid, active)
VALUES ('TEAM 4', '08612f06-bae2-4d2f-90d2-2254a68414b8', '09221c48-b916-47df-9aa0-a0194f86f6dd', TRUE),
       ('TEAM 5', '911adabe-5ab7-4470-8395-6b584a61462d', '09221c48-b916-47df-9aa0-a0194f86f6dd', TRUE),
       ('TEAM 6', '434a4e33-437f-4e6d-8f04-14ea40fdbfa2', '09221c48-b916-47df-9aa0-a0194f86f6dd', TRUE);

INSERT INTO unit_case_type (unit_uuid, case_type)
VALUES ('09221c48-b916-47df-9aa0-a0194f86f6dd', 'CT1'),
       ('09221c48-b916-47df-9aa0-a0194f86f6dd', 'CT2');

INSERT INTO permission (team_uuid, case_type, access_level)
VALUES ('08612f06-bae2-4d2f-90d2-2254a68414b8','CT1', 'OWNER'),
       ('08612f06-bae2-4d2f-90d2-2254a68414b8','CT2', 'READ'),
       ('911adabe-5ab7-4470-8395-6b584a61462d','CT1', 'WRITE'),
       ('434a4e33-437f-4e6d-8f04-14ea40fdbfa2','CT2', 'WRITE');

