delete from parent_topic where true;
delete from team_link where true;
delete from topic where true;
delete from team_contact where true;
delete from correspondent_type where true;
delete from permission where true;
delete from exemption_date where true;
delete from case_type_schema where true;
delete from stage_type_schema where true;
delete from stage_type where true;
delete from team where true;
delete from case_type where true;
delete from unit where true;
delete from constituency;
delete from region;

INSERT INTO region (UUID, region_name, active)
VALUES ('54321111-eeee-8436-3692-16782938a620', 'region', TRUE);

INSERT INTO constituency (UUID, constituency_name, region_uuid, active)
VALUES ('11111111-eeee-1111-1111-111111111111', 'test constituency 1', '54321111-eeee-8436-3692-16782938a620', TRUE),
       ('11111111-eeee-1111-1111-111111111112', 'test constituency 2', null, TRUE),
       ('11111111-eeee-1111-1111-111111111113', 'test constituency 3', null, TRUE),
       ('11111111-eeee-1111-1111-111111111114', 'test inactive constituency 4', null, FALSE);

INSERT INTO unit (display_name, uuid, short_code, active)
VALUES ('UNIT 2', '09221c48-b916-47df-9aa0-a0194f86f6dd', 'UNIT2', TRUE),
       ('UNIT 3', '65996106-91a5-44bf-bc92-a6c2f691f062', 'UNIT3', TRUE),
       ('UNIT 4', '10d5b353-a8ed-4530-bcc0-3edab0397d2f', 'UNIT4', TRUE),
       ('UNIT 5', 'c875dca8-8679-47e7-a589-7cea64b2e13c', 'DCU', TRUE),
       ('UNIT 6', 'e6eb3fec-c985-4160-b7f6-ff96be696781', 'PRIVATE_OFFICE', TRUE);

Insert INTO case_type (uuid, display_name, short_code, type, owning_unit_uuid, deadline_stage, active, bulk)
VALUES ('f62834a0-d231-44c9-bfa1-55dd93fc0aa0','Test Case Type 1', 'z9', 'CT1', '09221c48-b916-47df-9aa0-a0194f86f6dd', 'DISPATCH', true, true),
       ('056cf0eb-becd-49fa-86eb-ba4b7678a515','Test Case Type 2', 'za', 'CT2', '09221c48-b916-47df-9aa0-a0194f86f6dd', 'DISPATCH', true, true),
       ('692283fe-accd-40a5-9b6d-703e1aed7ebd','Test Case Type 3', 'zb', 'CT3', '09221c48-b916-47df-9aa0-a0194f86f6dd', 'DISPATCH', true, true),
       ('a3d491ff-3ee1-42be-bcad-840c4c4b9f0a','DCU Ministerial', 'a1', 'MIN', '09221c48-b916-47df-9aa0-a0194f86f6dd', 'DCU_MIN_INITIAL_DRAFT', true, true);

INSERT INTO team (display_name, letter_name, uuid, unit_uuid, active)
VALUES ('TEAM 4',null, '08612f06-bae2-4d2f-90d2-2254a68414b8', '09221c48-b916-47df-9aa0-a0194f86f6dd', TRUE),
       ('TEAM 5',null,  '911adabe-5ab7-4470-8395-6b584a61462d', '09221c48-b916-47df-9aa0-a0194f86f6dd', TRUE),
       ('TEAM 6',null,  '434a4e33-437f-4e6d-8f04-14ea40fdbfa2', '09221c48-b916-47df-9aa0-a0194f86f6dd', TRUE),
       ('TEAM 7',null,  '8b3b4366-a37c-48b6-b274-4c50f8083843', '09221c48-b916-47df-9aa0-a0194f86f6dd', TRUE),
       ('TEAM 8',null,  '5d584129-66ea-4e97-9277-7576ab1d32c0', '09221c48-b916-47df-9aa0-a0194f86f6dd', TRUE),
       ('TEAM 9',null,  '7c33c878-9404-4f67-9bbc-ca52dff285ca', '09221c48-b916-47df-9aa0-a0194f86f6dd', TRUE),
       ('TEAM 10',null, 'd09f1444-87ec-4197-8ec5-f28f548d11be', '10d5b353-a8ed-4530-bcc0-3edab0397d2f', FALSE);

INSERT INTO stage_type (uuid, display_name, short_code, type, case_type_uuid, acting_team_uuid, deadline, active)
VALUES ('a4f3226a-4845-4f41-b1d5-8d4d5146935e', 'stage type 1', 'x1', 'ST1', 'f62834a0-d231-44c9-bfa1-55dd93fc0aa0', 'd09f1444-87ec-4197-8ec5-f28f548d11be', 5, true),
       ('b4f3226a-4845-4f41-b1d5-8d4d5146935e', 'stage type 2', 'x2', 'ST2', 'f62834a0-d231-44c9-bfa1-55dd93fc0aa0', 'd09f1444-87ec-4197-8ec5-f28f548d11be', 5, true);


INSERT INTO permission (team_uuid, case_type, access_level)
VALUES ('08612f06-bae2-4d2f-90d2-2254a68414b8', 'CT1', 'OWNER'),
       ('08612f06-bae2-4d2f-90d2-2254a68414b8', 'CT2', 'READ'),
       ('911adabe-5ab7-4470-8395-6b584a61462d', 'CT1', 'WRITE'),
       ('434a4e33-437f-4e6d-8f04-14ea40fdbfa2', 'CT2', 'WRITE'),
       ('8b3b4366-a37c-48b6-b274-4c50f8083843', 'CT3', 'WRITE'),
       ('8b3b4366-a37c-48b6-b274-4c50f8083843', 'CT3', 'READ');


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
       ('test inactive topic 5 with inactive parent', '11111111-ffff-1111-1111-111111111135', '71caee7b-4632-4ac6-9c15-b91d4c0d27e5', FALSE);


INSERT INTO team_link (link_uuid, link_type, case_type, responsible_team_uuid, stage_type)
VALUES ('11111111-ffff-1111-1111-111111111131', 'TOPIC', 'MIN', '08612f06-bae2-4d2f-90d2-2254a68414b8' ,'ST1'),
       ('11111111-ffff-1111-1111-111111111132', 'TOPIC', 'MIN', '5d584129-66ea-4e97-9277-7576ab1d32c0' ,'ST1'),
       ('11111111-ffff-1111-1111-111111111131', 'TOPIC', 'MIN', '7c33c878-9404-4f67-9bbc-ca52dff285ca' ,'ST2'),
       ('11111111-ffff-1111-1111-111111111132', 'TOPIC', 'MIN', '5d584129-66ea-4e97-9277-7576ab1d32c0' ,'ST2'),
       ('54321111-eeee-8436-3692-16782938a620', 'REGION', 'MIN', '5d584129-66ea-4e97-9277-7576ab1d32c0' ,'ST1');


INSERT INTO team_contact (uuid, team_uuid, email_address)
VALUES ('37f76a1d-5366-4ab7-a7b1-a23b68c95b89', '08612f06-bae2-4d2f-90d2-2254a68414b8', 'one@email.com'),
       ('4ad178d7-b33d-4c4d-b9c8-cca63362380c', '08612f06-bae2-4d2f-90d2-2254a68414b8', 'two@email.com'),
       ('8bc0e84d-08e0-42f2-9d75-ff7b7c40d9fa', '911adabe-5ab7-4470-8395-6b584a61462d', 'three@email.com'),
       ('10c88bbc-00a7-4226-9d7b-bae5186debcd', '434a4e33-437f-4e6d-8f04-14ea40fdbfa2', 'four@email.com');

INSERT INTO correspondent_type (uuid, display_name, type)
VALUES ('fe1e8854-72ef-4141-a675-fb06760264fd', 'Test Correspondent Type 1', 'TEST1'),
       ('c2155e6c-7ecd-450d-86e3-884e48c8c6c7', 'Test Correspondent Type 2', 'TEST2');