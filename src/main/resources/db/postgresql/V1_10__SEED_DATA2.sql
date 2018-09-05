Insert INTO member (display_name, list_as, full_title, external_id, house, uuid, active)
VALUES
  ('Member1', 'Member 1 List', 'Member 1 full Title', 'ext1', 'COMMONS', '11111111-1111-1111-1112-111111111111', true),
  ('Member2', 'Member 2 List', 'Member 2 full Title', 'ext2', 'LORDS', '11111111-1111-1111-1112-111111111112', true),
  ('Member3', 'Member 3 List', 'Member 3 full Title', 'ext3', 'MEP', '11111111-1111-1111-1112-111111111113', true),
  ('Member4', 'Member 4 List', 'Member 4 full Title', 'ext4', 'SCOTISH', '11111111-1111-1111-1112-111111111114', true),
  ('Member5', 'Member 5 List', 'Member 5 full Title', 'ext5', 'WELSH', '11111111-1111-1111-1112-111111111115', true),
  ('Member6', 'Member 6 List', 'Member 6 full Title', 'ext6', 'COMMONS', '11111111-1111-1111-1112-111111111116', true),
  ('Member7', 'Member 7 List', 'Member 7 full Title', 'ext7', 'LORDS', '11111111-1111-1111-1112-111111111117', true),
  ('Member8', 'Member 8 List', 'Member 8 full Title', 'ext8', 'MEP', '11111111-1111-1111-1112-111111111118', true),
  ('Member9', 'Member 9 List', 'Member 9 full Title', 'ext9', 'SCOTISH', '11111111-1111-1111-1112-111111111119', true),
  ('Member10', 'Member 10 List', 'Member 10 full Title', 'ext10', 'WELSH', '11111111-1111-1111-1112-111111111110', FALSE );

INSERT INTO member_case_type (member_uuid, case_type)
VALUES
  ('11111111-1111-1111-1112-111111111111', 'MIN'),
  ('11111111-1111-1111-1112-111111111112', 'MIN'),
  ('11111111-1111-1111-1112-111111111113', 'TRO'),
  ('11111111-1111-1111-1112-111111111114', 'TRO'),
  ('11111111-1111-1111-1112-111111111115', 'DTEN'),
  ('11111111-1111-1111-1112-111111111116', 'MIN'),
  ('11111111-1111-1111-1112-111111111117', 'MIN'),
  ('11111111-1111-1111-1112-111111111118', 'TRO'),
  ('11111111-1111-1111-1112-111111111119', 'TRO'),
  ('11111111-1111-1111-1112-111111111110', 'DTEN');