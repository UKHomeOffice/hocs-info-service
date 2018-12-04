Insert INTO tenant (display_name, role)
VALUES ('DCU', 'DCU'),
       ('UKVI', 'UKVI');

Insert INTO case_type (display_name, short_code, type, tenant_role, case_deadline, active, bulk)
VALUES ('DCU Ministerial', 'a1', 'MIN', 'DCU', 'DCU_MIN_DISPATCH', true, true),
       ('DCU Treat Official', 'a2', 'TRO', 'DCU', 'DCU_TRO_DISPATCH', true, true),
       ('DCU Number 10', 'a3', 'DTEN', 'DCU', 'DCU_DTEN_DISPATCH', true, false),
       ('UKVI B REF', 'b1', 'IMCB', 'UKVI', 'UKVI_IMCB_DISPATCH', true, true),
       ('UKVI Ministerial REF', 'b2', 'IMCM', 'UKVI', 'UKVI_IMCM_DISPATCH', true, true),
       ('UKVI Number 10', 'b3', 'UTEN', 'UKVI', 'UKVI_UTEN_DISPATCH', true, true);

Insert INTO stage_type (display_name, short_code, type, tenant_role, active, deadline)
VALUES ('Data Input','111', 'DCU_MIN_DATA_INPUT', 'DCU', true, 10),
       ('Markup','112', 'DCU_MIN_MARKUP', 'DCU', true, 1),
       ('Transfer Confirmation','113', 'DCU_MIN_TRANSFER_CONFIRMATION', 'DCU', true, 3),
       ('No Reply Needed Confirmation','114', 'DCU_MIN_NO_REPLY_NEEDED_CONFIRMATION', 'DCU', true, 0),
       ('Initial Draft','115', 'DCU_MIN_INITIAL_DRAFT', 'DCU', true, 10),
       ('QA Response','116', 'DCU_MIN_QA_RESPONSE', 'DCU', true, 0),
       ('Private Office Approval','117', 'DCU_MIN_PRIVATE_OFFICE', 'DCU', true, 0),
       ('Ministerial Sign off','118', 'DCU_MIN_MINISTER_SIGN_OFF', 'DCU', true, 0),
       ('Dispatch','119', 'DCU_MIN_DISPATCH', 'DCU', true, 20),
       ('Copy To Number 10','11a', 'DCU_MIN_COPY_NUMBER_TEN', 'DCU', true, 0),
       ('Data Input','121', 'DCU_TRO_DATA_INPUT', 'DCU', true, 5),
       ('Markup','122', 'DCU_TRO_MARKUP', 'DCU', true, 10),
       ('Transfer Confirmation','123', 'DCU_TRO_TRANSFER_CONFIRMATION', 'DCU', true, 8),
       ('No Reply Needed Confirmation','124', 'DCU_TRO_NO_REPLY_NEEDED_CONFIRMATION', 'DCU', true, 10),
       ('Initial Draft','125', 'DCU_TRO_INITIAL_DRAFT', 'DCU', true, 10),
       ('QA Response','126', 'DCU_TRO_QA_RESPONSE', 'DCU', true, 3),
       ('Dispatch','127', 'DCU_TRO_DISPATCH', 'DCU', true, 10),
       ('Copy to Number 10','128', 'DCU_TRO_COPY_NUMBER_TEN', 'DCU', true, 0),
       ('Data Input','131', 'DCU_DTEN_DATA_INPUT', 'DCU', true, 0),
       ('Markup','132', 'DCU_DTEN_MARKUP', 'DCU', true, 0),
       ('Transfer Confirmation','133', 'DCU_DTEN_TRANSFER_CONFIRMATION', 'DCU', true, 0),
       ('No Reply Needed Confirmation','134', 'DCU_DTEN_NO_REPLY_NEEDED_CONFIRMATION', 'DCU', true, 10),
       ('Initial Draft','135', 'DCU_DTEN_INITIAL_DRAFT', 'DCU', true, 10),
       ('QA Response','136', 'DCU_DTEN_QA_RESPONSE', 'DCU', true, 10),
       ('Private Office','137', 'DCU_DTEN_PRIVATE_OFFICE', 'DCU', true, 0),
       ('Dispatch','138', 'DCU_DTEN_DISPATCH', 'DCU', true, 20),
       ('Copy To Number 10','139', 'DCU_DTEN_COPY_NUMBER_TEN', 'DCU', true, 10);

Insert INTO holiday_date (date, case_type)
VALUES ('2018-08-27', 'MIN'),
       ('2018-12-25', 'MIN'),
       ('2018-12-26', 'MIN'),
       ('2019-01-01', 'MIN'),
       ('2019-04-19', 'MIN'),
       ('2019-04-22', 'MIN'),
       ('2019-05-06', 'MIN'),
       ('2019-05-27', 'MIN'),
       ('2019-08-26', 'MIN'),
       ('2019-11-25', 'MIN'),
       ('2018-08-27', 'TRO'),
       ('2018-12-25', 'TRO'),
       ('2018-12-26', 'TRO'),
       ('2019-01-01', 'TRO'),
       ('2019-04-19', 'TRO'),
       ('2019-04-22', 'TRO'),
       ('2019-05-06', 'TRO'),
       ('2019-05-27', 'TRO'),
       ('2019-08-26', 'TRO'),
       ('2019-11-25', 'TRO'),
       ('2018-08-27', 'DTEN'),
       ('2018-12-25', 'DTEN'),
       ('2018-12-26', 'DTEN'),
       ('2019-01-01', 'DTEN'),
       ('2019-04-19', 'DTEN'),
       ('2019-04-22', 'DTEN'),
       ('2019-05-06', 'DTEN'),
       ('2019-05-27', 'DTEN'),
       ('2019-08-26', 'DTEN'),
       ('2019-11-25', 'DTEN');

INSERT INTO unit (display_name, uuid, short_code, active)
VALUES ('UNIT 1', 'd9a93c21-a1a8-4a5d-aa7b-597bb95a782c', 'UNIT1', TRUE);

INSERT INTO team (display_name, uuid, unit_uuid, active)
VALUES ('TEAM 1', '44444444-2222-2222-2222-222222222222', 'd9a93c21-a1a8-4a5d-aa7b-597bb95a782c', true),
       ('TEAM 2', '11111111-1111-1111-1111-111111111111', 'd9a93c21-a1a8-4a5d-aa7b-597bb95a782c', true),
       ('TEAM 3', '33333333-3333-3333-3333-333333333333', 'd9a93c21-a1a8-4a5d-aa7b-597bb95a782c', true);

INSERT INTO unit_case_type (unit_uuid, case_type)
VALUES ('d9a93c21-a1a8-4a5d-aa7b-597bb95a782c', 'MIN');

INSERT INTO permission (team_uuid, case_type, access_level)
VALUES ('44444444-2222-2222-2222-222222222222', 'MIN', 'OWNER');

INSERT INTO parent_topic (display_name, UUID)
VALUES ('Parent topic 1', '11111111-1111-1111-1111-111111111121'),
       ('Parent topic 2', '11111111-1111-1111-1111-111111111122');

INSERT INTO topic (display_name, UUID, parent_topic_uuid)
VALUES ('topic 1', '11111111-1111-1111-1111-111111111131', '11111111-1111-1111-1111-111111111121'),
       ('topic 2', '11111111-1111-1111-1111-111111111132', '11111111-1111-1111-1111-111111111122');

INSERT INTO parent_topic (display_name, UUID)
VALUES ('Parent topic 3', '11111111-1111-1111-1111-111111111123'),
       ('Parent topic 4', '11111111-1111-1111-1111-111111111124'),
       ('Parent topic 5', '11111111-1111-1111-1111-111111111125'),
       ('Parent topic 6', '11111111-1111-1111-1111-111111111126');

INSERT INTO topic (display_name, UUID, parent_topic_uuid)
VALUES ('topic 3', '11111111-1111-1111-1111-111111111133', '11111111-1111-1111-1111-111111111123'),
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

INSERT INTO parent_topic_case_type (parent_topic_uuid, case_type)
VALUES ('11111111-1111-1111-1111-111111111121', 'MIN'),
       ('11111111-1111-1111-1111-111111111122', 'MIN'),
       ('11111111-1111-1111-1111-111111111123', 'MIN'),
       ('11111111-1111-1111-1111-111111111124', 'MIN'),
       ('11111111-1111-1111-1111-111111111125', 'MIN'),
       ('11111111-1111-1111-1111-111111111126', 'MIN'),
       ('11111111-1111-1111-1111-111111111121', 'TRO'),
       ('11111111-1111-1111-1111-111111111122', 'TRO'),
       ('11111111-1111-1111-1111-111111111123', 'TRO'),
       ('11111111-1111-1111-1111-111111111124', 'TRO'),
       ('11111111-1111-1111-1111-111111111125', 'TRO'),
       ('11111111-1111-1111-1111-111111111126', 'TRO'),
       ('11111111-1111-1111-1111-111111111121', 'DTEN'),
       ('11111111-1111-1111-1111-111111111122', 'DTEN'),
       ('11111111-1111-1111-1111-111111111123', 'DTEN'),
       ('11111111-1111-1111-1111-111111111124', 'DTEN'),
       ('11111111-1111-1111-1111-111111111125', 'DTEN'),
       ('11111111-1111-1111-1111-111111111126', 'DTEN');

INSERT INTO correspondent_type (display_name, type)
VALUES ('Correspondent', 'CORRESPONDENT'),
       ('Constituent', 'CONSTITUENT'),
       ('Third Party', 'THIRD_PARY'),
       ('Applicant', 'APPLICANT'),
       ('Complainant', 'COMPLAINANT'),
       ('Family Relation', 'FAMILY'),
       ('Friend', 'FRIEND'),
       ('Legal Representative', 'LEGAL_REP'),
       ('Member', 'MEMBER'),
       ('Other', 'OTHER');

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
        'CF99 1N',
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

Insert INTO nominated_person (team_uuid, email_address)
VALUES ('44444444-2222-2222-2222-222222222222', 'edward.liddiard@homeoffice.gsi.gov.uk'),
       ('44444444-2222-2222-2222-222222222222', 'edward.liddiard@homeoffice.gsi.gov.uk'),
       ('11111111-1111-1111-1111-111111111111', 'edward.liddiard@homeoffice.gsi.gov.uk'),
       ('33333333-3333-3333-3333-333333333333', 'edward.liddiard@homeoffice.gsi.gov.uk');

Insert INTO minister (office_name, minister_name, uuid)
VALUES ('Home Secretary', 'Home Secretary', 'cba9013b-6862-417a-ad40-ebdf145601b1'),
       ('Minister for State for Immigration', 'Minister for State for Immigration',
        '2dada3ea-2530-4306-86cc-9cbade726048'),
       ('Minister of State for Security and Economic Crime', 'Minister of State for Security and Economic Crime',
        '2dada3ea-2530-4306-86cc-9cbade726041'),
       ('Minister of State for Policing and Fire Service', 'Minister of State for Policing and Fire Service',
        '2dada3ea-2530-4306-86cc-9cbade726042'),
       ('Under Secretary of State for Crime, Safeguarding and Vulnerability',
        'Under Secretary of State for Crime, Safeguarding and Vulnerability', '2dada3ea-2530-4306-86cc-9cbade726043'),
       ('Permanent Secretary', 'Permanent Secretary', '2dada3ea-2530-4306-86cc-9cbade726044'),
       ('Director General UKVI', 'Director General UKVI', '2dada3ea-2530-4306-86cc-9cbade726045'),
       ('Director Compliance and Returns Immigration Enforcement',
        'Director Compliance and Returns Immigration Enforcement', '2dada3ea-2530-4306-86cc-9cbade726046'),
       ('Director General Border Force', 'Director General Border Force', '2dada3ea-2530-4306-86cc-9cbade726047'),
       ('Director Resettlement Gold Command', 'Director Resettlement Gold Command',
        '2dada3ea-2530-4306-86cc-9cbade726056'),
       ('Director of UKVI International Operations', 'Director of UKVI International Operations',
        '2dada3ea-2530-4306-86cc-9cbade726098'),
       ('Director of UKVI Asylum', 'Director of UKVI Asylum', '2dada3ea-2530-4306-86cc-9cbade726099'),
       ('Minister for Lords', 'Minister for Lords', '2dada3ea-2530-4306-86cc-9cbade726000');
