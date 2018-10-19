INSERT INTO team (display_name, uuid, unit_uuid)
VALUES
       ('TEAM 2', '11111111-1111-1111-1111-111111111111', 'd9a93c21-a1a8-4a5d-aa7b-597bb95a782c'),
       ('TEAM 3', '33333333-3333-3333-3333-333333333333', 'd9a93c21-a1a8-4a5d-aa7b-597bb95a782c');


DELETE
FROM nominated_person;


Insert INTO nominated_person (teamUUID, email_address)
VALUES ('44444444-2222-2222-2222-222222222222', 'edward.liddiard@homeoffice.gsi.gov.uk'),
       ('44444444-2222-2222-2222-222222222222', 'edward.liddiard@homeoffice.gsi.gov.uk'),
       ('11111111-1111-1111-1111-111111111111', 'edward.liddiard@homeoffice.gsi.gov.uk'),
       ('33333333-3333-3333-3333-333333333333', 'edward.liddiard@homeoffice.gsi.gov.uk');
