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
