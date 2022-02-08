INSERT INTO unit (display_name, uuid, short_code, active)
VALUES ('UNIT 2', '09221c48-b916-47df-9aa0-a0194f86f6dd', 'UNIT2', TRUE),
       ('UNIT 3', '65996106-91a5-44bf-bc92-a6c2f691f062', 'UNIT3', TRUE),
       ('UNIT 4', '10d5b353-a8ed-4530-bcc0-3edab0397d2f', 'UNIT4', TRUE),
       ('UNIT 5', 'c875dca8-8679-47e7-a589-7cea64b2e13c', 'UNIT5', TRUE),
       ('UNIT 6', '66547972-56c6-4a8c-9bf5-b3debec1344a', 'UNIT6', TRUE);

Insert INTO case_type (uuid, display_name, short_code, type, owning_unit_uuid, deadline_stage, active, bulk, previous_case_type, initial_case)
VALUES ('f62834a0-d231-44c9-bfa1-55dd93fc0aa0','Test Case Type 1', 'z9', 'CT1', '09221c48-b916-47df-9aa0-a0194f86f6dd', 'DISPATCH', true, true, null, true),
       ('056cf0eb-becd-49fa-86eb-ba4b7678a515','Test Case Type 2', 'za', 'CT2', '09221c48-b916-47df-9aa0-a0194f86f6dd', 'DISPATCH', true, true, null, true),
       ('692283fe-accd-40a5-9b6d-703e1aed7ebd','Test Case Type 3', 'zb', 'CT3', '09221c48-b916-47df-9aa0-a0194f86f6dd', 'DISPATCH', true, true, null, true),
       ('d8a65dd9-1048-40ef-b5da-d6862e94a17e','Test Case Type 4', 'zc', 'CT4', '09221c48-b916-47df-9aa0-a0194f86f6dd', 'DISPATCH', true, true, 'CT3', false);

INSERT INTO team (display_name, letter_name, uuid, unit_uuid, active)
VALUES ('TEAM 4', null, '08612f06-bae2-4d2f-90d2-2254a68414b8', '09221c48-b916-47df-9aa0-a0194f86f6dd', TRUE),
       ('TEAM 5', null, '911adabe-5ab7-4470-8395-6b584a61462d', '09221c48-b916-47df-9aa0-a0194f86f6dd', TRUE),
       ('TEAM 6', null, '434a4e33-437f-4e6d-8f04-14ea40fdbfa2', '09221c48-b916-47df-9aa0-a0194f86f6dd', TRUE),
       ('TEAM 7', null, '8b3b4366-a37c-48b6-b274-4c50f8083843', '09221c48-b916-47df-9aa0-a0194f86f6dd', TRUE),
       ('TEAM 8', null, '5d584129-66ea-4e97-9277-7576ab1d32c0', '09221c48-b916-47df-9aa0-a0194f86f6dd', TRUE),
       ('TEAM 9', null, '7c33c878-9404-4f67-9bbc-ca52dff285ca', '09221c48-b916-47df-9aa0-a0194f86f6dd', TRUE),
       ('TEAM 10', null, 'd09f1444-87ec-4197-8ec5-f28f548d11be', '10d5b353-a8ed-4530-bcc0-3edab0397d2f', FALSE);

INSERT INTO stage_type (uuid, display_name, short_code, type, case_type_uuid, acting_team_uuid, deadline,
                        display_stage_order, active)
VALUES ('a4f3226a-4845-4f41-b1d5-8d4d5146935e', 'stage type 1', 'x1', 'ST1', 'f62834a0-d231-44c9-bfa1-55dd93fc0aa0',
        'd09f1444-87ec-4197-8ec5-f28f548d11be', 5, 1, true),
       ('b4f3226a-4845-4f41-b1d5-8d4d5146935e', 'stage type 2', 'x2', 'ST2', 'f62834a0-d231-44c9-bfa1-55dd93fc0aa0',
        'd09f1444-87ec-4197-8ec5-f28f548d11be', 5, 2, true),
       ('0905343c-a262-431d-b71a-45504ee9f3be', 'stage type 3', 'x3', 'DISPATCH', 'f62834a0-d231-44c9-bfa1-55dd93fc0aa0',
        'd09f1444-87ec-4197-8ec5-f28f548d11be', 5, 2, true);


INSERT INTO permission (team_uuid, case_type, access_level)
VALUES ('08612f06-bae2-4d2f-90d2-2254a68414b8', 'CT1', 'OWNER'),
       ('08612f06-bae2-4d2f-90d2-2254a68414b8', 'CT2', 'READ'),
       ('911adabe-5ab7-4470-8395-6b584a61462d', 'CT1', 'WRITE'),
       ('434a4e33-437f-4e6d-8f04-14ea40fdbfa2', 'CT2', 'WRITE'),
       ('8b3b4366-a37c-48b6-b274-4c50f8083843', 'CT3', 'WRITE'),
       ('8b3b4366-a37c-48b6-b274-4c50f8083843', 'CT3', 'READ'),
       ('8b3b4366-a37c-48b6-b274-4c50f8083843', 'CT4', 'READ');


INSERT INTO parent_topic (display_name, UUID, active)
VALUES ('test parent topic 100', '94a10f9f-a42e-44c0-8ebe-1227cb347f1d', TRUE),
       ('test parent topic 101', '1abf7a0c-ea2d-478d-b6c8-d739fb60ef04', TRUE),
       ('test inactive parent topic 102', '71caee7b-4632-4ac6-9c15-b91d4c0d27e5', FALSE),
       ('test parent Topic with no children 103', '038cecb8-00a2-4417-b18b-88c8905ff52e', TRUE);


INSERT INTO topic (display_name, UUID, parent_topic_uuid, active)
VALUES ('test topic 1', '11111111-ffff-1111-1111-111111111131', '94a10f9f-a42e-44c0-8ebe-1227cb347f1d', TRUE),
       ('test topic 2', '11111111-ffff-1111-1111-111111111132', '94a10f9f-a42e-44c0-8ebe-1227cb347f1d', TRUE),
       ('test topic 3', '11111111-ffff-1111-1111-111111111133', '1abf7a0c-ea2d-478d-b6c8-d739fb60ef04', TRUE),
       ('test inactive topic 4', '11111111-ffff-1111-1111-111111111134', '94a10f9f-a42e-44c0-8ebe-1227cb347f1d', FALSE),
       ('test inactive topic 5 with inactive parent', '11111111-ffff-1111-1111-111111111135',
        '71caee7b-4632-4ac6-9c15-b91d4c0d27e5', FALSE);


INSERT INTO team_link (link_value, link_type, case_type, responsible_team_uuid, stage_type)
VALUES ('11111111-ffff-1111-1111-111111111131', 'TOPIC', 'CT1', '08612f06-bae2-4d2f-90d2-2254a68414b8', 'ST1'),
       ('11111111-ffff-1111-1111-111111111132', 'TOPIC', 'CT1', '5d584129-66ea-4e97-9277-7576ab1d32c0', 'ST1'),
       ('11111111-ffff-1111-1111-111111111131', 'TOPIC', 'CT1', '7c33c878-9404-4f67-9bbc-ca52dff285ca', 'ST2'),
       ('11111111-ffff-1111-1111-111111111132', 'TOPIC', 'CT1', '5d584129-66ea-4e97-9277-7576ab1d32c0', 'ST2');


INSERT INTO team_contact (uuid, team_uuid, email_address)
VALUES ('37f76a1d-5366-4ab7-a7b1-a23b68c95b89', '08612f06-bae2-4d2f-90d2-2254a68414b8', 'one@email.com'),
       ('4ad178d7-b33d-4c4d-b9c8-cca63362380c', '08612f06-bae2-4d2f-90d2-2254a68414b8', 'two@email.com'),
       ('8bc0e84d-08e0-42f2-9d75-ff7b7c40d9fa', '911adabe-5ab7-4470-8395-6b584a61462d', 'three@email.com'),
       ('10c88bbc-00a7-4226-9d7b-bae5186debcd', '434a4e33-437f-4e6d-8f04-14ea40fdbfa2', 'four@email.com');

INSERT INTO correspondent_type (uuid, display_name, type)
VALUES ('fe1e8854-72ef-4141-a675-fb06760264fd', 'Test Correspondent Type 1', 'TEST1'),
       ('c2155e6c-7ecd-450d-86e3-884e48c8c6c7', 'Test Correspondent Type 2', 'TEST2');

--- schema test
CREATE OR REPLACE FUNCTION get_field_id_from_uuid(field_uuid UUID)
    RETURNS INTEGER AS '
DECLARE
result INTEGER;
BEGIN
    result := (SELECT id FROM info.field WHERE uuid = field_uuid);
RETURN result;
END;
' LANGUAGE plpgsql;

INSERT INTO info.screen_schema(uuid, type, title, action_label, active, props, validation)
VALUES ('f958f77d-b277-408d-bd6f-4a498d3f217f',
        'TEST_SCREEN_SCHEMA',
        'Test screen schema',
        'Confirm',
        true,
        '{}',
        '{}');

INSERT INTO info.field ("uuid", "component", "name", "label", "validation", "summary", "report_extract", "active", "props", child_field_id)
VALUES ('782a75de-ce06-4d31-95eb-87e42234f396',
        'text-area',
        'TEST_TEXT_FIELD',
        'Test text field',
        '[ "required" ]',
        false,
        false,
        true,
        '{}',
        null),
       ('932c2af5-55a1-430a-927a-56e7ef5f1743',
        'review-field',
        'TEST_REVIEW_FIELD',
        null,
        '[]'::jsonb, true, false, true,
        '{"direction": "TEST_DIRECTION"}'::jsonb,
        get_field_id_from_uuid('782a75de-ce06-4d31-95eb-87e42234f396'));

INSERT INTO info.field_screen(schema_uuid, field_uuid) VALUES
    ('f958f77d-b277-408d-bd6f-4a498d3f217f', '782a75de-ce06-4d31-95eb-87e42234f396'),
    ('f958f77d-b277-408d-bd6f-4a498d3f217f', '932c2af5-55a1-430a-927a-56e7ef5f1743');

-- Test for Active team retrieval

INSERT INTO info.unit ("uuid","display_name","short_code","active") VALUES
('5d153f3b-865d-49d9-a493-baedd241db19', 'UNIT_100', 'UNIT_100','true'),
('a95a4e2b-102b-4300-939b-1bb6c69e9989', 'UNIT_101', 'UNIT_101','true')
ON CONFLICT DO NOTHING;

INSERT INTO info.team ("unit_uuid","uuid","display_name","active") VALUES
('5d153f3b-865d-49d9-a493-baedd241db19','caf9f218-b914-4d98-b2a8-cff8df7a3bf1', 'TEAM_100', true),
('5d153f3b-865d-49d9-a493-baedd241db19','78714f46-57b9-42ff-af07-2dce5dc22f34', 'TEAM_101', true),
('5d153f3b-865d-49d9-a493-baedd241db19','4cd3ad27-6fc2-4d57-a928-a57971f381ba', 'TEAM_102', false),
('a95a4e2b-102b-4300-939b-1bb6c69e9989','84ef4756-f933-48a8-9aa6-59e6b04d141f', 'TEAM_103', true),
('a95a4e2b-102b-4300-939b-1bb6c69e9989','ca2b48e9-567d-49d2-a0f0-8061bb47e02d', 'TEAM_104', true)
ON CONFLICT DO NOTHING;


INSERT INTO info.case_type_action (uuid, case_type_uuid, case_type_type, action_type, action_label, active, max_concurrent_events, sort_order, props)
VALUES ('dd84d047-853b-428a-9ed7-94601623f345', 'f62834a0-d231-44c9-bfa1-55dd93fc0aa0','CT1', 'SUSPENSION','SUS 1', FALSE,1,10, '{}'::jsonb),
       ('dd84d047-853b-428a-9ed7-94601623f344', 'f62834a0-d231-44c9-bfa1-55dd93fc0aa0','CT1', 'EXTENSION','EXT 1', TRUE,1,10, '{}'::jsonb),
       ('dd84d047-853b-428a-9ed7-94601623f343', '056cf0eb-becd-49fa-86eb-ba4b7678a515','CT2', 'EXTENSION','EXT 1', TRUE,1,10, '{}'::jsonb),
       ('f2b625c9-7250-4293-9e68-c8f515e3043d', 'f62834a0-d231-44c9-bfa1-55dd93fc0aa0','CT1', 'APPEAL', 'APPEAL 1', TRUE,1,10, '{}'::jsonb);

INSERT INTO entity_list (uuid, display_name, simple_name)
VALUES ('9fda236f-6cd4-4016-b4af-307c424eaa50', 'Test entity list', 'TEST_ENTITIES')
    ON CONFLICT DO NOTHING;

INSERT INTO entity (entity_list_UUID, uuid, simple_name, data, sort_order)
VALUES  ('9fda236f-6cd4-4016-b4af-307c424eaa50','5abc65d9-3964-4c25-b570-46e203d5474b',
         'TEST_ENTITY_1', '{ "title" : "One"}'::jsonb, 1),
        ('9fda236f-6cd4-4016-b4af-307c424eaa50','8761bda1-8e26-4189-9133-aab3651aa584',
         'TEST_ENTITY_2', '{ "title" : "Two"}'::jsonb, 2);


INSERT INTO info.field("uuid", "component", "name", "label", "validation", "summary", "report_extract", "active", "props","access_level") VALUES
('39170e6b-591f-4ccd-b913-f9ad90d9f77a','dropdown','testDropdownActiveReadRestrict','Test Dropdown','[]',false,false,true,'{}','RESTRICTED_READ'),
('5e712bab-a52f-4dcc-95c7-332797406e53','dropdown','testDropdownNotActiveReadRestrict','Test Dropdown','[]',false,false,false,'{}','RESTRICTED_READ'),
('908f487c-cf73-4f54-a118-89ca16519320','dropdown','testDropdownActiveRead','Test Dropdown','[]',false,false,true,'{}','READ');

