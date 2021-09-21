delete from parent_topic where uuid in ('94a10f9f-a42e-44c0-8ebe-1227cb347f1d',
                                        '1abf7a0c-ea2d-478d-b6c8-d739fb60ef04',
                                        '71caee7b-4632-4ac6-9c15-b91d4c0d27e5',
                                        '038cecb8-00a2-4417-b18b-88c8905ff52e')
                            or display_name in ('Test Parent Topic');

delete from team_link where stage_type in ('ST1', 'ST2' );

delete from topic where uuid in ('11111111-ffff-1111-1111-111111111131',
                                 '11111111-ffff-1111-1111-111111111132',
                                 '11111111-ffff-1111-1111-111111111133',
                                 '11111111-ffff-1111-1111-111111111134',
                                 '11111111-ffff-1111-1111-111111111135')
                  or display_name in ('test topic');

delete from team_contact where team_uuid in ('08612f06-bae2-4d2f-90d2-2254a68414b8',
                                             '911adabe-5ab7-4470-8395-6b584a61462d',
                                             '434a4e33-437f-4e6d-8f04-14ea40fdbfa2');

delete from correspondent_type where uuid in ('fe1e8854-72ef-4141-a675-fb06760264fd',
                                              'c2155e6c-7ecd-450d-86e3-884e48c8c6c7');

delete from permission where case_type in ('CT1', 'CT2', 'CT3', 'CT4' );

delete from stage_type where type in ('ST1', 'ST2' );

delete from team where unit_uuid in ('09221c48-b916-47df-9aa0-a0194f86f6dd',
                                 '65996106-91a5-44bf-bc92-a6c2f691f062',
                                 '10d5b353-a8ed-4530-bcc0-3edab0397d2f',
                                 'c875dca8-8679-47e7-a589-7cea64b2e13c',
                                 '66547972-56c6-4a8c-9bf5-b3debec1344a');

delete from case_type where type in ('CT1', 'CT2', 'CT3', 'CT4' );

delete from unit where display_name in ('UNIT 2',
                                        'UNIT 3',
                                        'UNIT 4',
                                        'UNIT 5',
                                        'UNIT 6');

delete from correspondent_type where type in ('TEST', 'TEST1', 'TEST2');

--- clean up schema test
DROP FUNCTION IF EXISTS get_field_id_from_uuid(field_uuid UUID);

delete from field_screen where schema_uuid = 'f958f77d-b277-408d-bd6f-4a498d3f217f';

delete from screen_schema where uuid = 'f958f77d-b277-408d-bd6f-4a498d3f217f';

delete from field where uuid in ('782a75de-ce06-4d31-95eb-87e42234f396',
                                 '932c2af5-55a1-430a-927a-56e7ef5f1743');