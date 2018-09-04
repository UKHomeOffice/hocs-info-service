Insert INTO tenant (display_name, role)
VALUES
  ('DCU','DCU'),
  ('UKVI','UKVI');
Insert INTO case_type (display_name, type, tenant_role, active,bulk)
VALUES
  ('DCU Ministerial', 'MIN', 'DCU', true, true),
  ('DCU Treat Official', 'TRO', 'DCU', true, true),
  ('DCU Number 10', 'DTEN', 'DCU', true, false),
  ('UKVI B REF', 'IMCB', 'UKVI', true, true),
  ('UKVI Ministerial REF', 'IMCM', 'UKVI', true, true),
  ('UKVI Number 10', 'UTEN', 'UKVI', true, true);
Insert INTO sla (stage_type, value, case_type)
VALUES ('DCU_MIN_INITIAL_DRAFT', 10, 'MIN'),
  ('DCU_MIN_DISPATCH', 20, 'MIN'),
('DCU_TRO_INITIAL_DRAFT', 10, 'TRO'),
('DCU_TRO_DISPATCH', 20, 'TRO');
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

INSERT INTO unit(display_name, uuid, active)  VALUES
  ('UNIT 1'    , 'd9a93c21-a1a8-4a5d-aa7b-597bb95a782c',TRUE);

INSERT INTO team(display_name, uuid, unit_uuid) VALUES
  ('TEAM 1', '44444444-2222-2222-2222-222222222222', 'd9a93c21-a1a8-4a5d-aa7b-597bb95a782c');

INSERT INTO unit_case_type(unit_uuid,case_type) VALUES
  ('d9a93c21-a1a8-4a5d-aa7b-597bb95a782c', 'MIN');

Insert INTO nominated_person (teamUUID, email_address)
VALUES
  ('44444444-2222-2222-2222-222222222222','simulate-delivered@notifications.service.gov.uk'),
  ('44444444-2222-2222-2222-222222222222','simulate-delivered-2@notifications.service.gov.uk');

Insert INTO template (display_name, case_type, document_key, UUID,active)
VALUES
  ('MIN template', 'MIN', 'templates/min/MP_Response_template.odt', '11111111-1111-1111-1111-111111111111',TRUE ),
  ('TRO template', 'TRO', 'templates/tro/Treat_Official_template.odt', '11111111-1111-1111-1111-111111111113',TRUE ),
  ('DTEN template', 'DTEN', 'templates/dten/MP_Response_template.odt', '11111111-1111-1111-1111-111111111115',TRUE );

INSERT INTO parent_topic (display_name, UUID)
VALUES
  ('Parent topic 1', '11111111-1111-1111-1111-111111111121'),
  ('Parent topic 2', '11111111-1111-1111-1111-111111111122');

INSERT INTO topic (display_name, UUID, parent_topic_uuid)
VALUES
  ('topic 1', '11111111-1111-1111-1111-111111111131','11111111-1111-1111-1111-111111111121'),
  ('topic 2', '11111111-1111-1111-1111-111111111132','11111111-1111-1111-1111-111111111122');

INSERT INTO standard_line (display_name, document_key, UUID)
VALUES
  ('Standard Line 1','URL Link 1','11111111-1111-1111-1111-111111111141'),
  ('Standard Line 2','URL Link 2','11111111-1111-1111-1111-111111111142');

INSERT INTO standard_line_topic (standard_line_uuid, tenant_role,topic_uuid,active)
VALUES
  ('11111111-1111-1111-1111-111111111141','DCU','11111111-1111-1111-1111-111111111131',TRUE ),
  ('11111111-1111-1111-1111-111111111142','DCU','11111111-1111-1111-1111-111111111132',TRUE );
