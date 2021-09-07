------------------------------------------------------------------------
alter table case_type add "previous_case_type" text;

-- Update Stage 2 case and specify COMP case type predecessor
update case_type set previous_case_type = 'COMP' where uuid = '51463200-e01d-4573-87b5-514632000000';
