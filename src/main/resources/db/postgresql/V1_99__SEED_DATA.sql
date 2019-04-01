
INSERT INTO unit (display_name, uuid, short_code, active)
VALUES ('UNIT 1', 'd9a93c21-a1a8-4a5d-aa7b-597bb95a782c', 'UNIT1', TRUE),
       ('PRIVATE_UNIT', 'e1111111-5555-4a5d-aa7b-597bb95a782c', 'PRIVATE', TRUE),
       ('DCU_TEST', 'c875dca8-8679-47e7-a589-7cea64b2e13c', 'DCUT', TRUE),
       ('UKVI_TEST', '09c30d4b-b427-4b49-bec7-545eafb4019a', 'UKVIT', TRUE);

INSERT INTO team (display_name, uuid, unit_uuid, active)
VALUES ('TEAM 1', '44444444-2222-2222-2222-222222222222', 'd9a93c21-a1a8-4a5d-aa7b-597bb95a782c', true),
       ('TEAM 2', '11111111-1111-1111-1111-111111111111', 'd9a93c21-a1a8-4a5d-aa7b-597bb95a782c', true),
       ('TEAM 3', '33333333-3333-3333-3333-333333333333', 'd9a93c21-a1a8-4a5d-aa7b-597bb95a782c', true),
       ('TEAM 14', '33333333-4444-3333-3333-333333333333', 'd9a93c21-a1a8-4a5d-aa7b-597bb95a782c', true),
       ('PO TEAM 2', 'ffffffff-1111-1111-1111-111111111111', 'e1111111-5555-4a5d-aa7b-597bb95a782c', true),
       ('PO TEAM 3', 'ffffffff-3333-3333-3333-333333333333', 'e1111111-5555-4a5d-aa7b-597bb95a782c', true),
       ('PO TEAM 14', 'ffffffff-4444-3333-3333-333333333333', 'e1111111-5555-4a5d-aa7b-597bb95a782c', true);

Insert INTO case_type (uuid, display_name, short_code, type, owning_unit_uuid, deadline_stage, active, bulk)
VALUES ('a3d491ff-3ee1-42be-bcad-840c4c4b9f0a','DCU Ministerial', 'a1', 'MIN', 'c875dca8-8679-47e7-a589-7cea64b2e13c', 'DCU_MIN_DISPATCH', true, true),
       ('63c7215f-aefc-4492-aa08-7fe30959f95f','DCU Treat Official', 'a2', 'TRO', 'c875dca8-8679-47e7-a589-7cea64b2e13c', 'DCU_TRO_DISPATCH', true, true),
       ('814105e7-090a-4f1f-903a-62ad6b430bf1','DCU Number 10', 'a3', 'DTEN', 'c875dca8-8679-47e7-a589-7cea64b2e13c', 'DCU_DTEN_DISPATCH', true, false),
       ('4a1e6573-3084-4171-95df-e28fe90e940a','UKVI B REF', 'b1', 'IMCB', '09c30d4b-b427-4b49-bec7-545eafb4019a', 'UKVI_IMCB_DISPATCH', true, true),
       ('70899fb7-8a06-4954-bf38-98e4baec88c4','UKVI Ministerial REF', 'b2', 'IMCM' , '09c30d4b-b427-4b49-bec7-545eafb4019a', 'UKVI_IMCM_DISPATCH', true, true),
       ('ceed6981-6c37-4f7a-ba43-24314fc6d8f3','UKVI Number 10', 'b3', 'UTEN', '09c30d4b-b427-4b49-bec7-545eafb4019a', 'UKVI_UTEN_DISPATCH', true, true);

Insert INTO exemption_date (uuid, date, case_type_uuid)
VALUES ('7e5e4b76-8688-42f1-baa6-31cf8f247cb9','2018-08-27', 'a3d491ff-3ee1-42be-bcad-840c4c4b9f0a'),
       ('12795a82-64ac-449a-88f0-4069b08e89c6','2018-12-25', 'a3d491ff-3ee1-42be-bcad-840c4c4b9f0a'),
       ('59ff3fac-c8b4-40fd-b26a-f14dc62152ec','2018-12-26', 'a3d491ff-3ee1-42be-bcad-840c4c4b9f0a'),
       ('ca365b1c-17f8-4e62-8bf6-f0b3c4947141','2019-01-01', 'a3d491ff-3ee1-42be-bcad-840c4c4b9f0a'),
       ('802a5d00-2e29-4672-80b1-291e7d9d8735','2019-04-19', 'a3d491ff-3ee1-42be-bcad-840c4c4b9f0a'),
       ('7416d6be-1589-44e2-88ad-34f0b800b4ae','2019-04-22', 'a3d491ff-3ee1-42be-bcad-840c4c4b9f0a'),
       ('1bbe0a9c-2566-49a5-81f1-bd3a6873f110','2019-05-06', 'a3d491ff-3ee1-42be-bcad-840c4c4b9f0a'),
       ('4019f20e-0d0d-4dbc-a6c9-26fa23dcd23e','2019-05-27', 'a3d491ff-3ee1-42be-bcad-840c4c4b9f0a'),
       ('c27b58fe-5856-43fb-ba5e-a7253462ee60','2019-08-26', 'a3d491ff-3ee1-42be-bcad-840c4c4b9f0a'),
       ('b34caca3-9bed-4013-90f5-d9ab648ea4b5','2019-11-25', 'a3d491ff-3ee1-42be-bcad-840c4c4b9f0a'),
       ('3e340cd3-15d2-435b-875d-93ae6c88f3e6','2018-08-27', '63c7215f-aefc-4492-aa08-7fe30959f95f'),
       ('b60a456a-484e-469a-ae53-e4f9663b2750','2018-12-25', '63c7215f-aefc-4492-aa08-7fe30959f95f'),
       ('9a537527-68d9-48d4-9192-c26f3279729d','2018-12-26', '63c7215f-aefc-4492-aa08-7fe30959f95f'),
       ('dcf8a743-7b6e-432d-b51a-db2e4f22e6af','2019-01-01', '63c7215f-aefc-4492-aa08-7fe30959f95f'),
       ('2d46fa35-9d2e-4170-b4b4-dff344c1071a','2019-04-19', '63c7215f-aefc-4492-aa08-7fe30959f95f'),
       ('8c93cecf-8298-4a10-a9dc-ab0c6ec440ad','2019-04-22', '63c7215f-aefc-4492-aa08-7fe30959f95f'),
       ('788e8baa-acb5-4f29-83da-227349610bc3','2019-05-06', '63c7215f-aefc-4492-aa08-7fe30959f95f'),
       ('16e195c3-3e4c-408f-a0ee-f4112c10bb8c','2019-05-27', '63c7215f-aefc-4492-aa08-7fe30959f95f'),
       ('77aa6956-bdcc-4a2f-8345-6e383a2d1735','2019-08-26', '63c7215f-aefc-4492-aa08-7fe30959f95f'),
       ('48e2a673-eee3-4981-a441-d7dda3740a17','2019-11-25', '63c7215f-aefc-4492-aa08-7fe30959f95f'),
       ('7cbeab40-babe-4c5f-aa80-8e51733b986b','2018-08-27', '814105e7-090a-4f1f-903a-62ad6b430bf1'),
       ('2e9b2200-07d0-4a90-bc1c-bdeddd811623','2018-12-25', '814105e7-090a-4f1f-903a-62ad6b430bf1'),
       ('e4b51a4e-44e1-4417-b762-46df27cc1977','2018-12-26', '814105e7-090a-4f1f-903a-62ad6b430bf1'),
       ('808a585f-7848-4354-a543-7eb03e70ea3c','2019-01-01', '814105e7-090a-4f1f-903a-62ad6b430bf1'),
       ('92d75baf-b2b6-4b4b-a72f-0a0a34095c60','2019-04-19', '814105e7-090a-4f1f-903a-62ad6b430bf1'),
       ('d6862bb2-2360-4489-b4fe-78516a6b66d4','2019-04-22', '814105e7-090a-4f1f-903a-62ad6b430bf1'),
       ('2b94692d-c6bc-4bd5-86ef-5adfb61a6cbb','2019-05-06', '814105e7-090a-4f1f-903a-62ad6b430bf1'),
       ('36a31d3a-dae0-4262-8006-4a30d0216268','2019-05-27', '814105e7-090a-4f1f-903a-62ad6b430bf1'),
       ('c1125998-01a3-447c-87c5-752c3c67625b','2019-08-26', '814105e7-090a-4f1f-903a-62ad6b430bf1'),
       ('36bb5b7a-b371-413d-b6af-53ca64760188','2019-11-25', '814105e7-090a-4f1f-903a-62ad6b430bf1');

INSERT INTO unit_case_type (unit_uuid, case_type)
VALUES ('d9a93c21-a1a8-4a5d-aa7b-597bb95a782c', 'MIN');

INSERT INTO permission (team_uuid, case_type, access_level)
VALUES ('44444444-2222-2222-2222-222222222222', 'MIN', 'OWNER'),
       ('11111111-1111-1111-1111-111111111111', 'MIN', 'READ'),
       ('33333333-3333-3333-3333-333333333333', 'MIN', 'WRITE'),
       ('44444444-2222-2222-2222-222222222222', 'TRO', 'OWNER'),
       ('11111111-1111-1111-1111-111111111111', 'TRO', 'READ'),
       ('33333333-3333-3333-3333-333333333333', 'TRO', 'WRITE'),
       ('44444444-2222-2222-2222-222222222222', 'DTEN', 'OWNER'),
       ('11111111-1111-1111-1111-111111111111', 'DTEN', 'READ'),
       ('33333333-3333-3333-3333-333333333333', 'DTEN', 'WRITE'),
       ('44444444-2222-2222-2222-222222222222', 'IMCB', 'OWNER'),
       ('11111111-1111-1111-1111-111111111111', 'IMCB', 'READ'),
       ('33333333-3333-3333-3333-333333333333', 'IMCB', 'WRITE'),
       ('44444444-2222-2222-2222-222222222222', 'IMCM', 'OWNER'),
       ('11111111-1111-1111-1111-111111111111', 'IMCM', 'READ'),
       ('33333333-3333-3333-3333-333333333333', 'IMCM', 'WRITE'),
       ('44444444-2222-2222-2222-222222222222', 'UTEN', 'OWNER'),
       ('11111111-1111-1111-1111-111111111111', 'UTEN', 'READ'),
       ('33333333-3333-3333-3333-333333333333', 'UTEN', 'WRITE'),
       ('ffffffff-1111-1111-1111-111111111111', 'MIN', 'READ'),
       ('ffffffff-3333-3333-3333-333333333333', 'DTEN', 'READ'),
       ('ffffffff-4444-3333-3333-333333333333', 'TRO', 'READ');

Insert INTO stage_type (uuid, display_name, short_code, type, acting_team_uuid, active, deadline, case_type_uuid)
VALUES ('90eda1ba-86ba-4e55-b89b-8a5b14f72662','Data Input', '111', 'DCU_MIN_DATA_INPUT', '44444444-2222-2222-2222-222222222222', true, 10, 'a3d491ff-3ee1-42be-bcad-840c4c4b9f0a'),
       ('b80d0656-422a-404b-abdf-7a35932bca03','Markup', '112', 'DCU_MIN_MARKUP', '33333333-3333-3333-3333-333333333333', true, 1, 'a3d491ff-3ee1-42be-bcad-840c4c4b9f0a'),
       ('5c044c06-5c7a-489e-ae2d-2051196f9445','Transfer Confirmation', '113', 'DCU_MIN_TRANSFER_CONFIRMATION', '44444444-2222-2222-2222-222222222222', true, 3, 'a3d491ff-3ee1-42be-bcad-840c4c4b9f0a'),
       ('6ff95b19-8f46-4f72-909f-eee160d60a64','No Response Needed Confirmation', '114', 'DCU_MIN_NO_REPLY_NEEDED_CONFIRMATION', '33333333-3333-3333-3333-333333333333', true, 0, 'a3d491ff-3ee1-42be-bcad-840c4c4b9f0a'),
       ('906c1a90-8a85-42ed-b250-a5e69feb8dfd','Initial Draft', '115', 'DCU_MIN_INITIAL_DRAFT', '44444444-2222-2222-2222-222222222222', true, 10, 'a3d491ff-3ee1-42be-bcad-840c4c4b9f0a'),
       ('f98d142d-827b-4c8a-876e-3ffcccc464a2','QA Response', '116', 'DCU_MIN_QA_RESPONSE', '33333333-3333-3333-3333-333333333333', true, 0, 'a3d491ff-3ee1-42be-bcad-840c4c4b9f0a'),
       ('271353bf-baaf-40be-b18e-fd53fb5c54ad','Private Office Approval', '117', 'DCU_MIN_PRIVATE_OFFICE', '44444444-2222-2222-2222-222222222222', true, 0, 'a3d491ff-3ee1-42be-bcad-840c4c4b9f0a'),
       ('e5414378-5199-4326-ba37-99e86157c4ab','Ministerial Sign off', '118', 'DCU_MIN_MINISTER_SIGN_OFF', '44444444-2222-2222-2222-222222222222', true, 0, 'a3d491ff-3ee1-42be-bcad-840c4c4b9f0a'),
       ('3dfbf5ec-429b-4ede-8924-bb3270732914','Dispatch', '119', 'DCU_MIN_DISPATCH', '44444444-2222-2222-2222-222222222222', true, 20, 'a3d491ff-3ee1-42be-bcad-840c4c4b9f0a'),
       ('5528f481-0e30-4117-9429-acb95eada40a','Copy To Number 10', '11a', 'DCU_MIN_COPY_NUMBER_TEN', '44444444-2222-2222-2222-222222222222', true, 0, '63c7215f-aefc-4492-aa08-7fe30959f95f'),
       ('12b06e0c-ff86-45a8-b27d-59e40904c08c','Data Input', '121', 'DCU_TRO_DATA_INPUT', '44444444-2222-2222-2222-222222222222', true, 5,'63c7215f-aefc-4492-aa08-7fe30959f95f'),
       ('55415bea-8bb4-4d32-8bfc-bf5950397119','Markup', '122', 'DCU_TRO_MARKUP', '44444444-2222-2222-2222-222222222222', true, 10,'63c7215f-aefc-4492-aa08-7fe30959f95f'),
       ('0c2f1a5a-d288-4f96-a061-e24752fde73f','Transfer Confirmation', '123', 'DCU_TRO_TRANSFER_CONFIRMATION', '44444444-2222-2222-2222-222222222222', true, 8 ,'63c7215f-aefc-4492-aa08-7fe30959f95f'),
       ('d07d33c9-046a-4b06-981d-749675a6b152','No Response Needed Confirmation', '124', 'DCU_TRO_NO_REPLY_NEEDED_CONFIRMATION', '44444444-2222-2222-2222-222222222222', true, 10,'63c7215f-aefc-4492-aa08-7fe30959f95f'),
       ('fac2855d-f1cf-4e61-b488-4c690d92a83d','Initial Draft', '125', 'DCU_TRO_INITIAL_DRAFT', '33333333-3333-3333-3333-333333333333', true, 10,'63c7215f-aefc-4492-aa08-7fe30959f95f'),
       ('72c0ff10-0647-486c-8a7f-4abf70a26edc','QA Response', '126', 'DCU_TRO_QA_RESPONSE', '33333333-3333-3333-3333-333333333333', true, 3,'63c7215f-aefc-4492-aa08-7fe30959f95f'),
       ('140862b9-f4d0-4222-8e02-df2d78ba1d00','Dispatch', '127', 'DCU_TRO_DISPATCH', '44444444-2222-2222-2222-222222222222', true, 10 ,'63c7215f-aefc-4492-aa08-7fe30959f95f'),
       ('6f5a0dce-f25d-415a-8bd6-e5bd7e96d6ba','Copy to Number 10', '128', 'DCU_TRO_COPY_NUMBER_TEN', '44444444-2222-2222-2222-222222222222', true, 0,'63c7215f-aefc-4492-aa08-7fe30959f95f'),
       ('025fea5c-f8b9-4eb7-8ed8-c7aaa8eb72b0','Data Input', '131', 'DCU_DTEN_DATA_INPUT', '33333333-3333-3333-3333-333333333333', true, 0,'814105e7-090a-4f1f-903a-62ad6b430bf1'),
       ('d9985ff1-7508-4e32-bf00-17c0cf86fc1a','Markup', '132', 'DCU_DTEN_MARKUP', '33333333-3333-3333-3333-333333333333', true, 0,'814105e7-090a-4f1f-903a-62ad6b430bf1'),
       ('58d2706f-994b-4dfd-8f3c-1e7197ceb939','Transfer Confirmation', '133', 'DCU_DTEN_TRANSFER_CONFIRMATION', '33333333-3333-3333-3333-333333333333', true, 0,'814105e7-090a-4f1f-903a-62ad6b430bf1'),
       ('12db835f-4f0f-4bcd-9000-2b61b2f3c6eb','No Response Needed Confirmation', '134', 'DCU_DTEN_NO_REPLY_NEEDED_CONFIRMATION', '44444444-2222-2222-2222-222222222222', true, 10,'814105e7-090a-4f1f-903a-62ad6b430bf1'),
       ('21071956-6ea8-48e1-8933-56a5cbf99ffa','Initial Draft', '135', 'DCU_DTEN_INITIAL_DRAFT', '33333333-3333-3333-3333-333333333333', true, 10,'814105e7-090a-4f1f-903a-62ad6b430bf1'),
       ('c44bba20-9ed6-41ef-ba5f-cb454594c6b8','QA Response', '136', 'DCU_DTEN_QA_RESPONSE', '33333333-3333-3333-3333-333333333333', true, 10,'814105e7-090a-4f1f-903a-62ad6b430bf1'),
       ('2451e9a9-d79d-484a-bfe1-7b19dd85ea70','Private Office', '137', 'DCU_DTEN_PRIVATE_OFFICE', '33333333-3333-3333-3333-333333333333', true, 0,'814105e7-090a-4f1f-903a-62ad6b430bf1'),
       ('c00bc689-3626-47f9-a016-df7798c2fc46','Dispatch', '138', 'DCU_DTEN_DISPATCH', '33333333-3333-3333-3333-333333333333', true, 20,'814105e7-090a-4f1f-903a-62ad6b430bf1'),
       ('719c6230-1287-43db-a6a2-0d215d9253f0','Copy To Number 10', '139', 'DCU_DTEN_COPY_NUMBER_TEN', '33333333-3333-3333-3333-333333333333', true, 10,'814105e7-090a-4f1f-903a-62ad6b430bf1');


Insert INTO minister (office_name, minister_name, uuid, responsible_team_uuid)
VALUES ('Home Secretary', 'Home Secretary', 'cba9013b-6862-417a-ad40-ebdf145601b1', '44444444-2222-2222-2222-222222222222'),
       ('Minister for State for Immigration', 'Minister for State for Immigration','2dada3ea-2530-4306-86cc-9cbade726048', '44444444-2222-2222-2222-222222222222'),
       ('Minister of State for Security and Economic Crime', 'Minister of State for Security and Economic Crime','2dada3ea-2530-4306-86cc-9cbade726041', '44444444-2222-2222-2222-222222222222'),
       ('Minister of State for Policing and Fire Service', 'Minister of State for Policing and Fire Service','2dada3ea-2530-4306-86cc-9cbade726042', '44444444-2222-2222-2222-222222222222'),
       ('Under Secretary of State for Crime, Safeguarding and Vulnerability', 'Under Secretary of State for Crime, Safeguarding and Vulnerability', '2dada3ea-2530-4306-86cc-9cbade726043', '33333333-3333-3333-3333-333333333333'),
       ('Permanent Secretary', 'Permanent Secretary', '2dada3ea-2530-4306-86cc-9cbade726044', '33333333-3333-3333-3333-333333333333'),
       ('Director General UKVI', 'Director General UKVI', '2dada3ea-2530-4306-86cc-9cbade726045', '33333333-3333-3333-3333-333333333333'),
       ('Director Compliance and Returns Immigration Enforcement','Director Compliance and Returns Immigration Enforcement', '2dada3ea-2530-4306-86cc-9cbade726046', '33333333-3333-3333-3333-333333333333'),
       ('Director General Border Force', 'Director General Border Force', '2dada3ea-2530-4306-86cc-9cbade726047', '33333333-3333-3333-3333-333333333333'),
       ('Director Resettlement Gold Command', 'Director Resettlement Gold Command', '2dada3ea-2530-4306-86cc-9cbade726056', '33333333-3333-3333-3333-333333333333'),
       ('Director of UKVI International Operations', 'Director of UKVI International Operations','2dada3ea-2530-4306-86cc-9cbade726098', '33333333-3333-3333-3333-333333333333'),
       ('Director of UKVI Asylum', 'Director of UKVI Asylum', '2dada3ea-2530-4306-86cc-9cbade726099', '33333333-3333-3333-3333-333333333333'),
       ('Minister for Lords', 'Minister for Lords', '2dada3ea-2530-4306-86cc-9cbade726000', '33333333-3333-3333-3333-333333333333');


INSERT INTO parent_topic (display_name, UUID)
VALUES ('Parent topic 1', '11111111-1111-1111-1111-111111111121'),
       ('Parent topic 2', '11111111-1111-1111-1111-111111111122'),
       ('Parent topic 3', '11111111-1111-1111-1111-111111111123'),
       ('Parent topic 4', '11111111-1111-1111-1111-111111111124'),
       ('Parent topic 5', '11111111-1111-1111-1111-111111111125'),
       ('Parent topic 6', '11111111-1111-1111-1111-111111111126');

INSERT INTO topic (display_name, UUID, parent_topic_uuid)
VALUES ('topic 1', '11111111-1111-1111-1111-111111111131', '11111111-1111-1111-1111-111111111121'),
       ('topic 2', '11111111-1111-1111-1111-111111111132', '11111111-1111-1111-1111-111111111122'),
       ('topic 3', '11111111-1111-1111-1111-111111111133', '11111111-1111-1111-1111-111111111123'),
       ('topic 4', '11111111-1111-1111-1111-111111111134', '11111111-1111-1111-1111-111111111124'),
       ('topic 5', '11111111-1111-1111-1111-111111111135', '11111111-1111-1111-1111-111111111125'),
       ('topic 6', '11111111-1111-1111-1111-111111111136', '11111111-1111-1111-1111-111111111126'),
       ('topic 7', '11111111-1111-1111-1111-111111111137', '11111111-1111-1111-1111-111111111121'),
       ('topic 8', '11111111-1111-1111-1111-111111111138', '11111111-1111-1111-1111-111111111122'),
       ('topic 9', '11111111-1111-1111-1111-111111111139', '11111111-1111-1111-1111-111111111123'),
       ('topic 10', '11111111-1111-1111-1111-111111111140', '11111111-1111-1111-1111-111111111124'),
       ('topic 11', '11111111-1111-1111-1111-111111111141', '11111111-1111-1111-1111-111111111125'),
       ('topic 12', '11111111-1111-1111-1111-111111111142', '11111111-1111-1111-1111-111111111126'),
       ('topic 13', '11111111-1111-1111-1111-111111111143', '11111111-1111-1111-1111-111111111121'),
       ('topic 14', '11111111-1111-1111-1111-111111111144', '11111111-1111-1111-1111-111111111122'),
       ('topic 15', '11111111-1111-1111-1111-111111111145', '11111111-1111-1111-1111-111111111123'),
       ('topic 16', '11111111-1111-1111-1111-111111111146', '11111111-1111-1111-1111-111111111124'),
       ('topic 17', '11111111-1111-1111-1111-111111111147', '11111111-1111-1111-1111-111111111125'),
       ('topic 18', '11111111-1111-1111-1111-111111111148', '11111111-1111-1111-1111-111111111126');

INSERT INTO topic_team (topic_uuid, case_type, responsible_team_uuid, stage_type)
VALUES ('11111111-1111-1111-1111-111111111131', 'MIN', '44444444-2222-2222-2222-222222222222' ,'DCU_MIN_INITIAL_DRAFT'),
       ('11111111-1111-1111-1111-111111111132', 'MIN', '44444444-2222-2222-2222-222222222222' ,'DCU_MIN_INITIAL_DRAFT'),
       ('11111111-1111-1111-1111-111111111133', 'MIN', '33333333-4444-3333-3333-333333333333' ,'DCU_MIN_INITIAL_DRAFT'),
       ('11111111-1111-1111-1111-111111111134', 'MIN', '11111111-1111-1111-1111-111111111111' ,'DCU_MIN_INITIAL_DRAFT'),
       ('11111111-1111-1111-1111-111111111135', 'MIN', '11111111-1111-1111-1111-111111111111' ,'DCU_MIN_INITIAL_DRAFT'),
       ('11111111-1111-1111-1111-111111111136', 'MIN', '33333333-4444-3333-3333-333333333333' ,'DCU_MIN_INITIAL_DRAFT'),
       ('11111111-1111-1111-1111-111111111131', 'MIN', 'ffffffff-1111-1111-1111-111111111111' ,'DCU_MIN_PRIVATE_OFFICE'),
       ('11111111-1111-1111-1111-111111111133', 'MIN', 'ffffffff-4444-3333-3333-333333333333' ,'DCU_MIN_PRIVATE_OFFICE'),
       ('11111111-1111-1111-1111-111111111134', 'MIN', 'ffffffff-1111-1111-1111-111111111111' ,'DCU_MIN_PRIVATE_OFFICE'),
       ('11111111-1111-1111-1111-111111111135', 'MIN', 'ffffffff-1111-1111-1111-111111111111' ,'DCU_MIN_PRIVATE_OFFICE'),
       ('11111111-1111-1111-1111-111111111136', 'MIN', 'ffffffff-4444-3333-3333-333333333333' ,'DCU_MIN_PRIVATE_OFFICE'),
       ('11111111-1111-1111-1111-111111111131', 'DTEN', '44444444-2222-2222-2222-222222222222' ,'DCU_MIN_INITIAL_DRAFT'),
       ('11111111-1111-1111-1111-111111111132', 'DTEN', '44444444-2222-2222-2222-222222222222' ,'DCU_MIN_INITIAL_DRAFT'),
       ('11111111-1111-1111-1111-111111111133', 'DTEN', '33333333-4444-3333-3333-333333333333' ,'DCU_MIN_INITIAL_DRAFT'),
       ('11111111-1111-1111-1111-111111111134', 'DTEN', '11111111-1111-1111-1111-111111111111' ,'DCU_MIN_INITIAL_DRAFT'),
       ('11111111-1111-1111-1111-111111111135', 'DTEN', '11111111-1111-1111-1111-111111111111' ,'DCU_MIN_INITIAL_DRAFT'),
       ('11111111-1111-1111-1111-111111111136', 'DTEN', '33333333-4444-3333-3333-333333333333' ,'DCU_MIN_INITIAL_DRAFT'),
       ('11111111-1111-1111-1111-111111111131', 'DTEN', 'ffffffff-1111-1111-1111-111111111111' ,'DCU_MIN_PRIVATE_OFFICE'),
       ('11111111-1111-1111-1111-111111111133', 'DTEN', 'ffffffff-4444-3333-3333-333333333333' ,'DCU_MIN_PRIVATE_OFFICE'),
       ('11111111-1111-1111-1111-111111111134', 'DTEN', 'ffffffff-1111-1111-1111-111111111111' ,'DCU_MIN_PRIVATE_OFFICE'),
       ('11111111-1111-1111-1111-111111111135', 'DTEN', 'ffffffff-1111-1111-1111-111111111111' ,'DCU_MIN_PRIVATE_OFFICE'),
       ('11111111-1111-1111-1111-111111111136', 'DTEN', 'ffffffff-4444-3333-3333-333333333333' ,'DCU_MIN_PRIVATE_OFFICE'),
       ('11111111-1111-1111-1111-111111111131', 'TRO', '44444444-2222-2222-2222-222222222222' ,'DCU_MIN_INITIAL_DRAFT'),
       ('11111111-1111-1111-1111-111111111132', 'TRO', '44444444-2222-2222-2222-222222222222' ,'DCU_MIN_INITIAL_DRAFT'),
       ('11111111-1111-1111-1111-111111111133', 'TRO', '33333333-4444-3333-3333-333333333333' ,'DCU_MIN_INITIAL_DRAFT'),
       ('11111111-1111-1111-1111-111111111134', 'TRO', '11111111-1111-1111-1111-111111111111' ,'DCU_MIN_INITIAL_DRAFT'),
       ('11111111-1111-1111-1111-111111111135', 'TRO', '11111111-1111-1111-1111-111111111111' ,'DCU_MIN_INITIAL_DRAFT'),
       ('11111111-1111-1111-1111-111111111136', 'TRO', '33333333-4444-3333-3333-333333333333' ,'DCU_MIN_INITIAL_DRAFT'),
       ('11111111-1111-1111-1111-111111111131', 'TRO', 'ffffffff-1111-1111-1111-111111111111' ,'DCU_MIN_PRIVATE_OFFICE'),
       ('11111111-1111-1111-1111-111111111133', 'TRO', 'ffffffff-4444-3333-3333-333333333333' ,'DCU_MIN_PRIVATE_OFFICE'),
       ('11111111-1111-1111-1111-111111111134', 'TRO', 'ffffffff-1111-1111-1111-111111111111' ,'DCU_MIN_PRIVATE_OFFICE'),
       ('11111111-1111-1111-1111-111111111135', 'TRO', 'ffffffff-1111-1111-1111-111111111111' ,'DCU_MIN_PRIVATE_OFFICE'),
       ('11111111-1111-1111-1111-111111111136', 'TRO', 'ffffffff-4444-3333-3333-333333333333' ,'DCU_MIN_PRIVATE_OFFICE');

INSERT INTO correspondent_type (uuid, display_name, type)
VALUES ('85930589-0275-4bf9-aa59-48d4a1d334a0','Correspondent', 'CORRESPONDENT'),
       ('2604a33a-c9b3-49e9-a28e-98b9f21777d9','Constituent', 'CONSTITUENT'),
       ('78197b5a-5cf4-442a-900c-f770678609e9','Third Party', 'THIRD_PARTY'),
       ('a6b031c8-827c-4846-a5d9-9381a570610c','Applicant', 'APPLICANT'),
       ('a5c068ba-0b7c-4132-8160-88d838a6e12a','Complainant', 'COMPLAINANT'),
       ('04f88967-56e4-4719-a5d2-22e1d44b5b11','Family Relation', 'FAMILY'),
       ('a7e32412-79eb-436e-821d-3aa08a20b20b','Friend', 'FRIEND'),
       ('fb8d8d9d-3cd3-40a8-8764-8bf191c5e7c8','Legal Representative', 'LEGAL_REP'),
       ('d0ac3afa-997b-4a88-97f8-92fa79d28ae3','Member', 'MEMBER'),
       ('82fc0022-055c-4676-9116-f6a20f414a2c','Other', 'OTHER');

INSERT INTO house_address (uuid, house, house_code, address1, address2, address3, postcode, country, added, updated)
VALUES ('3cb5d7ca-2198-4444-afd8-4a870f2d0b12',
        'House of Commons',
        'HC',
        'House of Commons',
        'London',
        null,
        'SW1A 0AA',
        'United Kingdom',
        '2018-10-01',
        null),
       ('d1065a7e-c3ae-478c-8ab1-d3c0264496f4',
        'House of Lords',
        'HL',
        'House of Lords',
        'London',
        null,
        'SW1A 0PW',
        'United Kingdom',
        '2018-10-01',
        null),
       ('1ef82bf6-c403-45e9-89f4-5861604eaead',
        'Scottish Parliament',
        'SP',
        'The Scottish Parliament',
        'Edinburgh',
        null,
        'SEH99 1SP',
        'United Kingdom',
        '2018-10-01',
        null),
       ('3c2dbce1-6510-4a93-a855-b75f9538c929',
        'Welsh Assembly',
        'WA',
        'Welsh Government',
        '5th Floor, Tŷ Hywel',
        'Cardiff Bay',
        'CF99 1NA',
        'United Kingdom',
        '2018-10-01',
        null),
       ('925d4ff8-5d5a-4173-a7a8-1f955a7d54df',
        'Northern Irish Assembly',
        'NI',
        'Northern Ireland Assembly',
        'Parliament Buildings',
        'Belfast',
        'BT4 3XX',
        'United Kingdom',
        '2018-10-01',
        null),
       ('2c763346-3f41-4c49-8694-40033632d899',
        'European Parliament',
        'EU',
        'European Parliament Liaison Office in the UK',
        'Europe House, 32 Smith Square',
        'London',
        'SW1P 3EU',
        'United Kingdom',
        '2018-10-01',
        null);

Insert INTO team_contact (uuid, team_uuid, email_address)
VALUES ('ccd92f16-16d3-4b88-a4c8-e90e6cdf2a2a','44444444-2222-2222-2222-222222222222', 'edward.liddiard@homeoffice.gsi.gov.uk'),
       ('2a2dccd7-5ced-441b-ab16-f9c26bd77530','11111111-1111-1111-1111-111111111111', 'edward.liddiard@homeoffice.gsi.gov.uk'),
       ('3f078a1d-9583-48f8-886d-475224ae9cf5','33333333-3333-3333-3333-333333333333', 'edward.liddiard@homeoffice.gsi.gov.uk');

INSERT INTO screen_schema (uuid, type, active, title, action_label)
VALUES ('afa670fa-8048-4207-a0f6-35e856ffb70d', 'DCU_CORRESPONDENCE_DETAILS', true, 'Data Input', 'Continue'),
       ('4b9aa734-8048-4f77-9034-e0d9efaa2f77', 'DCU_CORRESPONDENCE_DETAILS_DTEN', true, 'Data Input', 'Continue'),
       ('acbec747-a86c-4812-877f-633e049aedc2', 'DCU_SET_PRIMARY_CORRESPONDENT', true, 'Data Input', 'Finish'),

       ('38740712-9873-4ece-ac60-e8f4f9f3ec53', 'DCU_MARKUP_DECISION', true, 'Markup', 'Continue'),
       ('56c6ee9f-216d-42da-910e-df68fe56276c', 'DCU_MARKUP_OGD_DETAILS', true, 'Markup', 'Finish'),
       ('a47bb3a8-e9c2-4e84-b3e2-458dd95d8139', 'DCU_MARKUP_NRN_DETAILS', true, 'Markup', 'Finish'),
       ('03341d10-55c6-41c3-936d-519ebe9ee762', 'TOPICS', true, 'Markup', 'Continue'),
       ('07257739-4796-45a5-a462-1d3ecb516a32', 'DCU_ANSWERING', true, 'Markup', 'Finish'),

       ('d1119106-b5ef-4db1-9f77-5fc8be01333d', 'TRANSFER_CONFIRMATION', true, 'Transfer To OGD', 'Finish'),

       ('faf134ae-a3f8-41bd-9aaf-52c7393d1a52', 'NO_REPLY_NEEDED_CONFIRMATION', true, 'No Response Needed', 'Finish'),

       ('241780f4-0fcc-4297-8ad0-e60230ba84d1', 'DCU_INITIAL_DRAFT_DECISION', true, 'Initial Draft', 'Continue'),
       ('3c3b0dc0-ad8a-4be1-a377-aa20fbd67f20', 'DCU_RESPONSE_CHANNEL', true, 'Initial Draft', 'Continue'),
       ('63518bde-0bd0-4389-8eeb-4ea6744f4193', 'DCU_DRAFT_REJECTION_NOTE', true, 'Initial Draft', 'Finish'),
       ('0dd19543-a0f7-411e-a9bf-568e90a1db91', 'DCU_DRAFT_PHONECALL_NOTE', true, 'Initial Draft', 'Finish'),
       ('df60288f-1eda-4420-aa61-d819f2697293', 'DCU_UPLOAD_DOCUMENT', true, 'Initial Draft', 'Continue'),
       ('5e0695b1-b0d4-43cc-915c-c67a1da0cf76', 'DCU_OFFLINE_QA_DECISION', true, 'Initial Draft', 'Continue'),
       ('f26ee9cc-a9b7-460d-bd66-898a90ed0ee6', 'DCU_OFFLINE_QA_DETAILS', true, 'Initial Draft', 'Finish'),

       ('8f1d3c22-9aea-4319-9357-0f485dfc2223', 'APPROVE_QA_RESPONSE', true, 'QA Response', 'Continue'),
       ('0fe10690-02cb-4264-b4c9-c090f24d47c2', 'DCU_QA_REJECTION_NOTE', true, 'QA Response', 'Finish'),

       ('0d18c411-d86e-433f-9a91-6eb0c7e2d129', 'APPROVE_PRIVATE_OFFICE', true, 'Private Office Sign Off', 'Continue'),
       ('0f990142-4856-4564-8eb0-ece85e2c10cf', 'APPROVE_PRIVATE_OFFICE_DTEN', true, 'Private Office Sign Off', 'Continue'),

       ('f145b5a1-8635-4f3a-b411-065f53e71fa5', 'DCU_PRIVATE_OFFICE_REJECTION_NOTE', true, 'Private Office Sign Off', 'Finish'),
       ('9aa32d23-3e5a-43e0-b415-0c4db40a9ab0', 'CHANGE_MINISTER', true, 'Private Office Sign Off', 'Finish') ,

       ('6dffa215-2107-432d-a784-e54655af2c56', 'APPROVE_MINISTER_SIGN_OFF', true, 'Minister Sign Off', 'Continue')  ,
       ('bdf78e93-2cde-44d0-b5c7-04186c5d71b7', 'APPROVE_MINISTER_SIGN_OFF_REJECTION_NOTE', true, 'Minister Sign Off', 'Continue'),

       ('47f0f8a0-71b1-42b4-be3c-1908121c3fb5', 'APPROVE_DISPATCH', true, 'Dispatch', 'Continue'),

       ('1c14536c-ded9-41b8-8b39-e558087970e1', 'DCU_DISPATCH_REJECTION_NOTE', true, 'Dispatch', 'Finish'),

       ('2212fd30-f34e-43b8-a904-23e3ffbc885a', 'DISPATCH', true, 'Dispatch', 'Finish'),

       ('fa52bcc8-93b8-44cb-837f-82e8343f5b0f', 'DCU_DTEN_APPROVE_DISPATCH', true, 'Dispatch', 'Continue'),

       ('dedbf417-5604-4c5f-9731-9d4d23599c9b', 'COPY_NUMBER_TEN_RESPONSE', true, 'Copy To Number Ten', 'Finish')

;


INSERT INTO case_type_schema (schema_uuid, case_type)
VALUES ('afa670fa-8048-4207-a0f6-35e856ffb70d', 'MIN'),
       ('afa670fa-8048-4207-a0f6-35e856ffb70d', 'TRO'),
       ('4b9aa734-8048-4f77-9034-e0d9efaa2f77', 'DTEN'),
       ('acbec747-a86c-4812-877f-633e049aedc2', 'MIN'),
       ('acbec747-a86c-4812-877f-633e049aedc2', 'TRO'),
       ('acbec747-a86c-4812-877f-633e049aedc2', 'DTEN'),
       ('38740712-9873-4ece-ac60-e8f4f9f3ec53', 'MIN'),
       ('38740712-9873-4ece-ac60-e8f4f9f3ec53', 'TRO'),
       ('38740712-9873-4ece-ac60-e8f4f9f3ec53', 'DTEN'),
       ('56c6ee9f-216d-42da-910e-df68fe56276c', 'MIN'),
       ('56c6ee9f-216d-42da-910e-df68fe56276c', 'TRO'),
       ('56c6ee9f-216d-42da-910e-df68fe56276c', 'DTEN'),
       ('a47bb3a8-e9c2-4e84-b3e2-458dd95d8139', 'MIN'),
       ('a47bb3a8-e9c2-4e84-b3e2-458dd95d8139', 'TRO'),
       ('a47bb3a8-e9c2-4e84-b3e2-458dd95d8139', 'DTEN'),
       ('03341d10-55c6-41c3-936d-519ebe9ee762', 'MIN'),
       ('03341d10-55c6-41c3-936d-519ebe9ee762', 'TRO'),
       ('03341d10-55c6-41c3-936d-519ebe9ee762', 'DTEN'),
       ('07257739-4796-45a5-a462-1d3ecb516a32', 'MIN'),
       ('07257739-4796-45a5-a462-1d3ecb516a32', 'TRO'),
       ('07257739-4796-45a5-a462-1d3ecb516a32', 'DTEN'),
       ('241780f4-0fcc-4297-8ad0-e60230ba84d1', 'MIN'),
       ('241780f4-0fcc-4297-8ad0-e60230ba84d1', 'TRO'),
       ('241780f4-0fcc-4297-8ad0-e60230ba84d1', 'DTEN'),
       ('3c3b0dc0-ad8a-4be1-a377-aa20fbd67f20', 'MIN'),
       ('3c3b0dc0-ad8a-4be1-a377-aa20fbd67f20', 'TRO'),
       ('3c3b0dc0-ad8a-4be1-a377-aa20fbd67f20', 'DTEN'),
       ('63518bde-0bd0-4389-8eeb-4ea6744f4193', 'MIN'),
       ('63518bde-0bd0-4389-8eeb-4ea6744f4193', 'TRO'),
       ('63518bde-0bd0-4389-8eeb-4ea6744f4193', 'DTEN'),
       ('0dd19543-a0f7-411e-a9bf-568e90a1db91', 'MIN'),
       ('0dd19543-a0f7-411e-a9bf-568e90a1db91', 'TRO'),
       ('df60288f-1eda-4420-aa61-d819f2697293', 'MIN'),
       ('df60288f-1eda-4420-aa61-d819f2697293', 'TRO'),
       ('df60288f-1eda-4420-aa61-d819f2697293', 'DTEN'),
       ('5e0695b1-b0d4-43cc-915c-c67a1da0cf76', 'MIN'),
       ('5e0695b1-b0d4-43cc-915c-c67a1da0cf76', 'DTEN'),
       ('f26ee9cc-a9b7-460d-bd66-898a90ed0ee6', 'MIN'),
       ('f26ee9cc-a9b7-460d-bd66-898a90ed0ee6', 'DTEN'),
       ('8f1d3c22-9aea-4319-9357-0f485dfc2223', 'MIN'),
       ('8f1d3c22-9aea-4319-9357-0f485dfc2223', 'TRO'),
       ('8f1d3c22-9aea-4319-9357-0f485dfc2223', 'DTEN'),
       ('6dffa215-2107-432d-a784-e54655af2c56', 'MIN'),
       ('6dffa215-2107-432d-a784-e54655af2c56', 'DTEN'),
       ('0fe10690-02cb-4264-b4c9-c090f24d47c2', 'MIN'),
       ('0fe10690-02cb-4264-b4c9-c090f24d47c2', 'TRO'),
       ('0fe10690-02cb-4264-b4c9-c090f24d47c2', 'DTEN'),
       ('d1119106-b5ef-4db1-9f77-5fc8be01333d', 'MIN'),
       ('d1119106-b5ef-4db1-9f77-5fc8be01333d', 'TRO'),
       ('d1119106-b5ef-4db1-9f77-5fc8be01333d', 'DTEN'),
       ('faf134ae-a3f8-41bd-9aaf-52c7393d1a52', 'MIN'),
       ('faf134ae-a3f8-41bd-9aaf-52c7393d1a52', 'TRO'),
       ('faf134ae-a3f8-41bd-9aaf-52c7393d1a52', 'DTEN'),
       ('0d18c411-d86e-433f-9a91-6eb0c7e2d129', 'MIN'),
       ('0d18c411-d86e-433f-9a91-6eb0c7e2d129', 'TRO'),
       ('0f990142-4856-4564-8eb0-ece85e2c10cf', 'DTEN'),
       ('f145b5a1-8635-4f3a-b411-065f53e71fa5', 'MIN'),
       ('f145b5a1-8635-4f3a-b411-065f53e71fa5', 'DTEN'),
       ('9aa32d23-3e5a-43e0-b415-0c4db40a9ab0', 'MIN'),
       ('6dffa215-2107-432d-a784-e54655af2c56', 'MIN'),
       ('6dffa215-2107-432d-a784-e54655af2c56', 'DTEN'),
       ('bdf78e93-2cde-44d0-b5c7-04186c5d71b7', 'MIN'),
       ('bdf78e93-2cde-44d0-b5c7-04186c5d71b7', 'DTEN'),
       ('47f0f8a0-71b1-42b4-be3c-1908121c3fb5', 'MIN'),
       ('47f0f8a0-71b1-42b4-be3c-1908121c3fb5', 'TRO'),
       ('fa52bcc8-93b8-44cb-837f-82e8343f5b0f', 'DTEN'),
       ('1c14536c-ded9-41b8-8b39-e558087970e1', 'MIN'),
       ('1c14536c-ded9-41b8-8b39-e558087970e1', 'TRO'),
       ('1c14536c-ded9-41b8-8b39-e558087970e1', 'DTEN'),
       ('2212fd30-f34e-43b8-a904-23e3ffbc885a', 'MIN'),
       ('2212fd30-f34e-43b8-a904-23e3ffbc885a', 'TRO'),
       ('2212fd30-f34e-43b8-a904-23e3ffbc885a', 'DTEN'),
       ('dedbf417-5604-4c5f-9731-9d4d23599c9b', 'MIN'),
       ('dedbf417-5604-4c5f-9731-9d4d23599c9b', 'TRO')
;

INSERT INTO field (uuid, component, name, label, validation, summary, active, props)
VALUES ('03548dc4-76bb-4ac8-8992-b555fd59fa0a', 'date', 'DateOfCorrespondence', 'When was the correspondence sent?', '[ "required" ]',  true, true,  '{}'),
       ('1cb5ee23-b82d-448d-8574-00421841acdc', 'date', 'DateReceived', 'When was the correspondence received?', '[ "required" ]', true, true, '{}'),
       ('ede4aa5d-80d5-4703-aeed-82167d927ad7', 'radio', 'OriginalChannel', 'How was the correspondence received?', '[ "required" ]', true, true,  '{ "choices" : [ {  "label" : "Email", "value" : "EMAIL" }, { "label" : "Post", "value" : "POST" }, { "label" : "Phone", "value" : "PHONE" }, { "label" : "No. 10", "value" : "NO10" } ]}'),
       ('157a00e6-3b96-4a12-9b34-f73c328c332c', 'radio', 'CopyNumberTen', 'Should the response be copied to Number 10?','[]', true, true, '{ "choices" : [ {  "label" : "Yes", "value" : "TRUE" }, { "label" : "No", "value" : "FALSE" }]}'),

       ('dc502507-c333-43be-a562-f6d0e456502e', 'entity-list', 'Correspondents', 'Which is the primary correspondent?','[ "required" ]', false, true, '{ "hasRemoveLink" : true, "hasAddLink" : true, "action" : "CORRESPONDENT", "choices" : "CASE_CORRESPONDENTS", "entity" : "correspondent" }'),
       ('1d8ae1c0-8ebf-4185-8fda-b7dc2eefbc6a', 'radio', 'MarkupDecision', 'What sort of response is required?','[ "required" ]', false, true, '{"choices" : [ {  "label" : "Policy Response", "value" : "PR" }, { "label" : "FAQ Response", "value" : "FAQ" }, { "label" : "Refer To OGD", "value" : "OGD" }, { "label" : "No Response Needed", "value" : "NRN"} ] }'),
       ('20086634-e128-407c-a22d-fc845b9074dc', 'text', 'OGDDept', 'Where should this case be transferred to?','[ "required" ]', false, true, '{}'),
       ('975c19f8-523b-4f28-b9cc-f60f49c359ef', 'text-area', 'CaseNote_OGD', 'Why should this case be transferred here?','[ "required" ]', false, true, '{}'),
       ('453c088e-f75a-4d08-a85e-b18a092a9157', 'text', 'OGDDept', 'Where should this case be transferred to?','[]', false, true, '{ "disabled" : "disabled"}'),
       ('f8a7f43d-7f0f-499a-871c-63c4143600f7', 'text-area', 'CaseNote_OGD', 'Why should this case be transferred here?','[ "required" ]', false, true, '{ "disabled" : "disabled"}'),
       ('91542958-9e9d-432a-93d7-a60d594ce1a1', 'text-area', 'CaseNote_NRN', 'Why is no response needed?','[ "required" ]', false, true, '{}'),

       ('1cf94194-54d5-4f6a-aaff-2e84a511a2cc', 'radio', 'TransferConfirmation', 'Should this case be transferred to the OGD?', '[ "required" ]', false, true,  '{ "choices" : [ {  "label" : "Yes", "value" : "ACCEPT" }, { "label" : "No", "value" : "REJECT" }]}'),

       ('1e792dfd-7838-43d9-9ec3-80e4ffc44887', 'text-area', 'CaseNote_NRN', 'Why is no response needed?','[ ]', false, true, '{ "disabled" : "disabled"}'),
       ('eade08a7-8697-4dca-acca-ecb58b4794bc', 'radio', 'NoReplyNeededConfirmation', 'Do you agree that no response is needed?', '[ "required" ]', false, true,  '{ "choices" : [ {  "label" : "Yes", "value" : "ACCEPT" }, { "label" : "No", "value" : "REJECT" }]}'),
       ('f9ec3e4a-7332-493c-a26f-7e541b8fe032', 'inset', null, null,'[ ]', false, true, '{ "children" : "If you select ''No'' the case will be returned to Markup" }'),

       ('ffcff056-36ee-483f-825d-9cf0f4a9ff75', 'entity-list', 'Topics', 'Which is the primary topic?','[ "required" ]', false, true, '{ "hasRemoveLink" : true, "hasAddLink" : true, "action" : "TOPIC", "choices" : "CASE_TOPICS", "entity" : "topic" }'),

       ('c0a9fb65-ec01-40a1-9df7-a70779352715', 'inset', null, null,'[ ]', false, true, '{ "children" : "These teams have been selected based on the correspondence topic. You are able to override the selection." }'),
       ('c2837864-53a0-4373-9695-f1f5e23552a0', 'text', 'DraftingTeamName', 'Initial Draft Team','[ ]', false, true, '{ "disabled" : "disabled"}'),

       ('cb170956-1fac-4b92-8572-d2db6c97e72a', 'dropdown', 'OverrideDraftingTeamUUID', 'Override Initial Draft Team','[ ]', false, true, '{ "choices" : "DRAFT_TEAMS" }'),
       ('760e7c6c-17ca-420b-badf-1833f59feeed', 'text', 'POTeamName', 'Private Office Team','[ ]', false, true, '{ "disabled" : "disabled"}'),
       ('b8baa4c7-d6bc-4657-ab6f-ae237b73247b', 'dropdown', 'OverridePOTeamUUID', 'Override Private Office Team','[ ]', false, true, '{ "choices" : "PRIVATE_OFFICE_TEAMS" }'),

       ('16bcc0f3-1cbd-4570-9fdb-7d91e008ad94', 'radio', 'InitialDraftDecision', 'Can this correspondence be answered by your team?','[ "required" ]', false, true, '{ "choices" : [ {  "label" : "Yes", "value" : "ACCEPT" }, { "label" : "No", "value" : "REJECT" } ] }'),
       ('d650ff40-9000-448b-ac99-04f762e8768d', 'inset', null, null,'[ ]', false, true, '{ "children" : "If you select ''No'' you will be asked to explain why and the case will be returned to Markup." }'),
       ('57864e64-c4b3-43b0-a0ab-65e7dc24ea93', 'radio', 'ResponseChannel', 'How do you intend to respond?','["required" ]', false, true, '{"choices" : [ {  "label" : "Letter", "value" : "LETTER" }, { "label" : "Phone", "value" : "PHONE" } ] }'),
       ('3f9f8ce7-7caa-4a9f-982d-27f2aa8e2126', 'inset', null, null,'[ ]', false, true, '{ "children" : "If you select ''Phone'' you will be asked to summarise the call and then the case will close." }'),
       ('256d086a-c4fd-48d6-8dd6-0bf760cf66ea', 'text-area', 'CaseNote_RejectionNote', 'Why should this should not be answered by your team?','[ "required" ]', false, true, '{}'),
       ('fe7befd2-7873-44c6-824c-43e529e1b805', 'inset', null, null,'[ ]', false, true, '{ "children" : "The case will be returned to DCU Markup along with your comments." }'),

       ('b3ea7f48-a7f5-417e-81b5-46ee2f6a2cf9', 'inset', null, null,'[ ]', false, true, '{ "children" : "Completing this screen will complete the case." }'),
       ('7edd64b1-6790-41d4-842e-fa9921979278', 'text-area', 'CaseNote_PhonecallNote', 'Please summarise your call.','[ "required" ]', false, true, '{}'),
       ('d33dfc8e-8c8a-4fcc-be30-f98184299661', 'entity-manager', 'Documents_standard_line', 'Available Standard line','[ ]', false, true, '{ "hasDownloadLink" : true, "choices" : "CASE_STANDARD_LINES", "entity" : "standard_line" }'),
       ('f55e66c5-180b-411c-b908-5397e8fade19', 'entity-manager', 'Documents_topic', 'Available Template','[ ]', false, true, '{ "hasDownloadLink" : true, "choices" : "CASE_TEMPLATES", "entity" : "template" }'),
       ('7f525f9e-36d2-4ff2-b888-e3a18c2c6bfa', 'entity-list', 'DraftDocuments', 'Which is the primary draft document?','[ "required" ]', false, true, '{ "hasRemoveLink" : true, "hasAddLink" : true, "choices" : "CASE_DOCUMENT_LIST_DRAFT", "entity" : "document" }'),

       ('d91196a8-1a7a-4451-be05-09f0a3b95a23', 'radio', 'OfflineQA', 'Do you want to QA this offline?','["required" ]', false, true, '{"choices" : [ { "label" : "Yes", "value" : "TRUE" }, {  "label" : "No", "value" : "FALSE" } ] }'),
       ('07559a09-505b-42c4-984c-112f60b42c37', 'inset', null, null,'[ ]', false, true, '{ "children" : "If you select ''Yes'' you will be asked to name the person who has done the QA stage." }'),
       ('9ab22c05-58ec-4bc6-8e7e-e70bfc34b028', 'inset', null, null,'[ ]', false, true, '{ "children" : "If you select ''No'' the case will be returned to your team queue for the QA stage." }'),

       ('3d4ac394-ed36-48ed-aea8-7aa2dd546414', 'dropdown', 'OfflineQaUser', 'Who has done the Offline QA for this case?' ,'[ "required" ]', false, true, '{ "choices" : "USERS_FOR_CASE" }'),

       ('8494130c-1373-4422-8656-b30ae443d5d3', 'radio', 'QAResponseDecision', 'Do you approve the response?','["required" ]', false, true, '{"choices" : [ {  "label" : "Yes", "value" : "ACCEPT" }, { "label" : "No", "value" : "REJECT" } ] }'),

       ('6a388279-4950-42cd-8a2a-e92d5c932964', 'inset', null, null,'[ ]', false, true, '{ "children" : "If you select ''No'' you will be asked to explain why and the case will be returned to the Draft stage." }'),

       ('13e1ab9b-8e92-4309-b0f7-7e2668d8dd3b', 'text-area', 'CaseNote_QA', 'What is your feedback about the response?','[ "required" ]', false, true, '{}'),

       ('6370740e-8ec5-4907-9179-86d5b31e4ec4', 'radio', 'PrivateOfficeDecision', 'Do you approve the response?','["required" ]', false, true, '{"choices" : [ {  "label" : "Yes", "value" : "ACCEPT" }, { "label" : "No", "value" : "REJECT" }, { "label" : "Change Minister", "value" : "CHANGE"} ] }'),
       ('f0152ce3-96db-4790-9f78-2c6a21c96c67', 'radio', 'PrivateOfficeDecision', 'Do you approve the response?','["required" ]', false, true, '{"choices" : [ {  "label" : "Yes", "value" : "ACCEPT" }, { "label" : "No", "value" : "REJECT" } ] }'),

       ('0ffde77e-ff96-40c1-aab3-08102063d63d', 'inset', null, null,'[ ]', false, true, '{ "children" : "If you select ''No'' you will be asked to explain why and the case will be returned to the Draft stage." }'),
       ('4a9aa75d-719b-4c60-8608-af84c6d06048', 'inset', null, null,'[ ]', false, true, '{ "children" : "If you select ''Change Minister'' you will be asked to explain why and the case will be assigned to that Private Office team." }'),

       ('0f08cabb-a822-411c-8352-765dd20e252c', 'text-area', 'CaseNote_PrivateOfficeReject', 'What is your feedback about the response?','[ "required" ]', false, true, '{}'),

       ('bd3ff7f7-8b27-45e8-8636-e0ec6f0b40dc', 'dropdown', 'PrivateOfficeOverridePOTeamUUID', 'Override Private Office Team','[ "required" ]', false, true, '{ "choices" : "PRIVATE_OFFICE_TEAMS" }'),
       ('d0070438-7fb8-498b-bd5e-63fa07027256', 'text-area', 'CaseNote_PrivateOfficeOverride', 'Why should this be approved by this team instead?','[ "required" ]', false, true, '{}'),

       ('f2cd5c44-ba32-4b75-adfb-ea83f384373b', 'radio', 'MinisterSignOffDecision', 'Do you approve the response?','["required" ]', false, true, '{"choices" : [ {  "label" : "Yes", "value" : "ACCEPT" }, { "label" : "No", "value" : "REJECT" } ] }'),
       ('0660e228-a914-4ab2-91fa-15c9a417810c', 'inset', null, null,'[ ]', false, true, '{ "children" : "If you select ''No'' you will be asked to explain why and the case will be returned to the Draft stage." }'),

       ('ee4b9198-5194-4ba9-bf6a-e9a63c970e10', 'text-area', 'CaseNote_MinisterReject', 'What is your feedback about the response?','[ "required" ]', false, true, '{}'),

       ('f098de70-7ea0-44b7-86a4-e2fb5715fee5', 'text', 'ResponseChannel', 'How do you intend to respond?','[ ]', false, true, '{ "disabled" : "disabled" }'),

       ('a572f7cd-7b1b-4020-a0ca-b83178f86cd8', 'radio', 'DispatchDecision', 'Are you able to dispatch this?','["required" ]', false, true, '{"choices" : [ {  "label" : "Yes", "value" : "ACCEPT" }, { "label" : "No", "value" : "REJECT" } ] }'),
       ('3f84ba5c-167f-4963-be71-556a66f20296', 'inset', null, null,'[ ]', false, true, '{ "children" : "If you select ''No'' you will be asked to explain why and the case will be returned." }'),

       ('967ba403-1c6a-4620-9ac5-d61f00c433c5', 'text-area', 'CaseNote_DispatchDecisionReject', 'Why are you unable to dispatch this?','[ "required" ]', false, true, '{}');


INSERT INTO field_screen(schema_uuid, field_uuid)
VALUES ('afa670fa-8048-4207-a0f6-35e856ffb70d', '03548dc4-76bb-4ac8-8992-b555fd59fa0a'),
       ('afa670fa-8048-4207-a0f6-35e856ffb70d', '1cb5ee23-b82d-448d-8574-00421841acdc'),
       ('afa670fa-8048-4207-a0f6-35e856ffb70d', 'ede4aa5d-80d5-4703-aeed-82167d927ad7'),
       ('afa670fa-8048-4207-a0f6-35e856ffb70d', '157a00e6-3b96-4a12-9b34-f73c328c332c'),

       ('4b9aa734-8048-4f77-9034-e0d9efaa2f77', '03548dc4-76bb-4ac8-8992-b555fd59fa0a'),
       ('4b9aa734-8048-4f77-9034-e0d9efaa2f77', '1cb5ee23-b82d-448d-8574-00421841acdc'),
       ('4b9aa734-8048-4f77-9034-e0d9efaa2f77', 'ede4aa5d-80d5-4703-aeed-82167d927ad7'),

       ('acbec747-a86c-4812-877f-633e049aedc2', 'dc502507-c333-43be-a562-f6d0e456502e'),

       ('38740712-9873-4ece-ac60-e8f4f9f3ec53', '1d8ae1c0-8ebf-4185-8fda-b7dc2eefbc6a'),

       ('a47bb3a8-e9c2-4e84-b3e2-458dd95d8139', '91542958-9e9d-432a-93d7-a60d594ce1a1'),

       ('56c6ee9f-216d-42da-910e-df68fe56276c', '20086634-e128-407c-a22d-fc845b9074dc'),
       ('56c6ee9f-216d-42da-910e-df68fe56276c', '975c19f8-523b-4f28-b9cc-f60f49c359ef'),

       ('03341d10-55c6-41c3-936d-519ebe9ee762', 'ffcff056-36ee-483f-825d-9cf0f4a9ff75'),

       ('07257739-4796-45a5-a462-1d3ecb516a32', 'c0a9fb65-ec01-40a1-9df7-a70779352715'),
       ('07257739-4796-45a5-a462-1d3ecb516a32', 'b8baa4c7-d6bc-4657-ab6f-ae237b73247b'),
       ('07257739-4796-45a5-a462-1d3ecb516a32', 'cb170956-1fac-4b92-8572-d2db6c97e72a'),
       ('07257739-4796-45a5-a462-1d3ecb516a32', '760e7c6c-17ca-420b-badf-1833f59feeed'),
       ('07257739-4796-45a5-a462-1d3ecb516a32', 'c2837864-53a0-4373-9695-f1f5e23552a0'),

       ('241780f4-0fcc-4297-8ad0-e60230ba84d1', '16bcc0f3-1cbd-4570-9fdb-7d91e008ad94'),
       ('241780f4-0fcc-4297-8ad0-e60230ba84d1', 'd650ff40-9000-448b-ac99-04f762e8768d'),

       ('3c3b0dc0-ad8a-4be1-a377-aa20fbd67f20', '57864e64-c4b3-43b0-a0ab-65e7dc24ea93'),
       ('3c3b0dc0-ad8a-4be1-a377-aa20fbd67f20', '3f9f8ce7-7caa-4a9f-982d-27f2aa8e2126'),

       ('63518bde-0bd0-4389-8eeb-4ea6744f4193', '256d086a-c4fd-48d6-8dd6-0bf760cf66ea'),
       ('63518bde-0bd0-4389-8eeb-4ea6744f4193', 'fe7befd2-7873-44c6-824c-43e529e1b805'),

       ('0dd19543-a0f7-411e-a9bf-568e90a1db91', 'b3ea7f48-a7f5-417e-81b5-46ee2f6a2cf9'),
       ('0dd19543-a0f7-411e-a9bf-568e90a1db91', '7edd64b1-6790-41d4-842e-fa9921979278'),

       ('df60288f-1eda-4420-aa61-d819f2697293', 'd33dfc8e-8c8a-4fcc-be30-f98184299661'),
       ('df60288f-1eda-4420-aa61-d819f2697293', 'f55e66c5-180b-411c-b908-5397e8fade19'),
       ('df60288f-1eda-4420-aa61-d819f2697293', '7f525f9e-36d2-4ff2-b888-e3a18c2c6bfa'),

       ('f26ee9cc-a9b7-460d-bd66-898a90ed0ee6', '3d4ac394-ed36-48ed-aea8-7aa2dd546414'),

       ('8f1d3c22-9aea-4319-9357-0f485dfc2223', '8494130c-1373-4422-8656-b30ae443d5d3'),
       ('8f1d3c22-9aea-4319-9357-0f485dfc2223', '6a388279-4950-42cd-8a2a-e92d5c932964'),

       ('0fe10690-02cb-4264-b4c9-c090f24d47c2', '13e1ab9b-8e92-4309-b0f7-7e2668d8dd3b'),

       ('d1119106-b5ef-4db1-9f77-5fc8be01333d', '453c088e-f75a-4d08-a85e-b18a092a9157'),
       ('d1119106-b5ef-4db1-9f77-5fc8be01333d', 'f8a7f43d-7f0f-499a-871c-63c4143600f7'), 
       ('d1119106-b5ef-4db1-9f77-5fc8be01333d', 'b30434dd-60da-4730-9c1a-eb0c1b417bcd'),
       ('d1119106-b5ef-4db1-9f77-5fc8be01333d', '1cf94194-54d5-4f6a-aaff-2e84a511a2cc'),
       ('d1119106-b5ef-4db1-9f77-5fc8be01333d', 'f9ec3e4a-7332-493c-a26f-7e541b8fe032'),

       ('faf134ae-a3f8-41bd-9aaf-52c7393d1a52', '1e792dfd-7838-43d9-9ec3-80e4ffc44887'),
       ('faf134ae-a3f8-41bd-9aaf-52c7393d1a52', 'eade08a7-8697-4dca-acca-ecb58b4794bc'),
       ('faf134ae-a3f8-41bd-9aaf-52c7393d1a52', 'f9ec3e4a-7332-493c-a26f-7e541b8fe032'),

       ('5e0695b1-b0d4-43cc-915c-c67a1da0cf76', 'd91196a8-1a7a-4451-be05-09f0a3b95a23'),
       ('5e0695b1-b0d4-43cc-915c-c67a1da0cf76', '07559a09-505b-42c4-984c-112f60b42c37'),
       ('5e0695b1-b0d4-43cc-915c-c67a1da0cf76', '9ab22c05-58ec-4bc6-8e7e-e70bfc34b028'),

       ('0d18c411-d86e-433f-9a91-6eb0c7e2d129', '6370740e-8ec5-4907-9179-86d5b31e4ec4'),
       ('0d18c411-d86e-433f-9a91-6eb0c7e2d129', '0ffde77e-ff96-40c1-aab3-08102063d63d'),
       ('0d18c411-d86e-433f-9a91-6eb0c7e2d129', '4a9aa75d-719b-4c60-8608-af84c6d06048'),

       ('0f990142-4856-4564-8eb0-ece85e2c10cf', 'f0152ce3-96db-4790-9f78-2c6a21c96c67'),
       ('0f990142-4856-4564-8eb0-ece85e2c10cf', '0ffde77e-ff96-40c1-aab3-08102063d63d'),

       ('f145b5a1-8635-4f3a-b411-065f53e71fa5', '0f08cabb-a822-411c-8352-765dd20e252c'),

       ('9aa32d23-3e5a-43e0-b415-0c4db40a9ab0', 'bd3ff7f7-8b27-45e8-8636-e0ec6f0b40dc'),
       ('9aa32d23-3e5a-43e0-b415-0c4db40a9ab0', 'd0070438-7fb8-498b-bd5e-63fa07027256'),

       ('6dffa215-2107-432d-a784-e54655af2c56', 'f2cd5c44-ba32-4b75-adfb-ea83f384373b'),
       ('6dffa215-2107-432d-a784-e54655af2c56', '0660e228-a914-4ab2-91fa-15c9a417810c'),

       ('bdf78e93-2cde-44d0-b5c7-04186c5d71b7', 'ee4b9198-5194-4ba9-bf6a-e9a63c970e10'),

       ('47f0f8a0-71b1-42b4-be3c-1908121c3fb5', 'f098de70-7ea0-44b7-86a4-e2fb5715fee5'),
       ('47f0f8a0-71b1-42b4-be3c-1908121c3fb5', 'a572f7cd-7b1b-4020-a0ca-b83178f86cd8'),
       ('47f0f8a0-71b1-42b4-be3c-1908121c3fb5', '3f84ba5c-167f-4963-be71-556a66f20296'),

       ('fa52bcc8-93b8-44cb-837f-82e8343f5b0f', 'a572f7cd-7b1b-4020-a0ca-b83178f86cd8'),
       ('fa52bcc8-93b8-44cb-837f-82e8343f5b0f', '3f84ba5c-167f-4963-be71-556a66f20296'),

       ('1c14536c-ded9-41b8-8b39-e558087970e1', '967ba403-1c6a-4620-9ac5-d61f00c433c5');
