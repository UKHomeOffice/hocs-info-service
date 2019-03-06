DELETE FROM info.parent_topic WHERE UUID = '94a10f9f-a42e-44c0-8ebe-1227cb347f1d' OR UUID = '1abf7a0c-ea2d-478d-b6c8-d739fb60ef04' OR UUID = '71caee7b-4632-4ac6-9c15-b91d4c0d27e5' OR display_name = 'Test Parent Topic';

DELETE FROM info.permission WHERE case_type = 'CT1' OR case_type = 'CT2' OR case_type = 'CT3';
DELETE FROM info.topic_team WHERE true;
DELETE FROM info.topic WHERE true;
DELETE FROM info.case_type WHERE type = 'CT1' OR type = 'CT2' OR type = 'CT3';
DELETE FROM info.team_contact WHERE team_uuid = '08612f06-bae2-4d2f-90d2-2254a68414b8' OR team_uuid = '911adabe-5ab7-4470-8395-6b584a61462d' OR team_uuid = '434a4e33-437f-4e6d-8f04-14ea40fdbfa2';
DELETE FROM info.team WHERE unit_uuid = '09221c48-b916-47df-9aa0-a0194f86f6dd' OR unit_uuid = '65996106-91a5-44bf-bc92-a6c2f691f062' OR unit_uuid = '10d5b353-a8ed-4530-bcc0-3edab0397d2f';
DELETE FROM info.unit WHERE uuid = '09221c48-b916-47df-9aa0-a0194f86f6dd' OR uuid = '65996106-91a5-44bf-bc92-a6c2f691f062' OR uuid = '10d5b353-a8ed-4530-bcc0-3edab0397d2f';

