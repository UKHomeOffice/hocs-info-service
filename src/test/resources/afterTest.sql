DELETE FROM parent_topic WHERE UUID = '94a10f9f-a42e-44c0-8ebe-1227cb347f1d' OR UUID = '1abf7a0c-ea2d-478d-b6c8-d739fb60ef04';

DELETE FROM permission WHERE case_type = 'CT1' OR case_type = 'CT2' OR case_type = 'CT3';
DELETE FROM topic_team WHERE true;
DELETE FROM topic WHERE true;
DELETE FROM case_type WHERE type = 'CT1' OR type = 'CT2' OR type = 'CT3';
DELETE FROM team WHERE unit_uuid = '09221c48-b916-47df-9aa0-a0194f86f6dd' OR unit_uuid = '65996106-91a5-44bf-bc92-a6c2f691f062' OR unit_uuid = '10d5b353-a8ed-4530-bcc0-3edab0397d2f';
DELETE FROM unit WHERE uuid = '09221c48-b916-47df-9aa0-a0194f86f6dd' OR uuid = '65996106-91a5-44bf-bc92-a6c2f691f062' OR uuid = '10d5b353-a8ed-4530-bcc0-3edab0397d2f';

