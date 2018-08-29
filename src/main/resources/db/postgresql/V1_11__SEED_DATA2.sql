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