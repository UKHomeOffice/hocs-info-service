DROP TABLE IF EXISTS entity_list;

CREATE TABLE IF NOT EXISTS entity_list
(
  id            BIGSERIAL PRIMARY KEY,
  uuid          UUID      NOT NULL,
  display_name  TEXT      NOT NULL,
  simple_name   TEXT      NOT NULL,
  active        BOOLEAN   NOT NULL DEFAULT TRUE,

  CONSTRAINT entity_list_uuid_idempotent UNIQUE (uuid),
  CONSTRAINT entity_list_simple_name_idempotent UNIQUE (simple_name)

);

DROP TABLE IF EXISTS entity;

CREATE TABLE IF NOT EXISTS entity
(
  id                      BIGSERIAL PRIMARY KEY,
  uuid                    UUID      NOT NULL,
  simple_name             TEXT      NOT NULL,
  data                    JSONB     NOT NULL DEFAULT '{}',
  entity_list_uuid        UUID,
  active                  BOOLEAN   NOT NULL DEFAULT TRUE,

  CONSTRAINT entity_uuid_idempotent UNIQUE (uuid),
  CONSTRAINT entity_simple_name_idempotent UNIQUE (simple_name),
  CONSTRAINT fk_entity_entity_list FOREIGN KEY (entity_list_UUID) REFERENCES entity_list (uuid)

);

CREATE INDEX entity_uuid_simple_name ON entity (uuid, simple_name);


DROP TABLE IF EXISTS entity_relation;

CREATE TABLE entity_relation
(
  id                  BIGSERIAL PRIMARY KEY,
  entity_uuid         UUID,
  parent_entity_uuid  UUID,

  CONSTRAINT fk_entity_relation_entity FOREIGN KEY (entity_uuid) REFERENCES entity (uuid),
  CONSTRAINT fk_entity_relation_parent_entity FOREIGN KEY (parent_entity_uuid) REFERENCES entity (uuid)
);

CREATE INDEX entity_relation_entity ON entity_relation (entity_uuid);
CREATE INDEX entity_relation_parent_entity ON entity_relation (parent_entity_uuid);
CREATE INDEX entity_relation_entity_parent_entity ON entity_relation (entity_uuid, parent_entity_uuid);


-- ########### UNITS LIST
INSERT INTO entity_list (uuid, display_name, simple_name)
VALUES ('6b22043d-564b-473d-8385-5a7081fc04fe', 'Units', 'unit');

INSERT INTO  entity (uuid, simple_name, entity_list_UUID, data)
VALUES ('7dbd7eed-df69-4601-a88d-00f569021704', 'DCU', '6b22043d-564b-473d-8385-5a7081fc04fe', '{ "displayName" : "DCU" }'),
       ('ffe5c6ba-be21-4f8e-9b5f-abfc7fd6f05a', 'UKVI','6b22043d-564b-473d-8385-5a7081fc04fe', '{ "displayName" : "UKVI" }');

-- ########### CASETYPES LIST
INSERT INTO entity_list (uuid, display_name, simple_name)
VALUES ('af606e56-0707-4b11-9bdf-687e94f7e5f1', 'Case Types', 'caseType');

INSERT INTO entity (uuid, simple_name, entity_list_UUID, data)
VALUES ('28c09fd9-c6cc-4c94-8906-2876c6b99388', 'MIN', 'af606e56-0707-4b11-9bdf-687e94f7e5f1', '{ "displayName" : "DCU Ministerial", "shortCode" : "a1", "bulk" : true }'),
       ('d7c4bb49-9ff2-4193-a704-6e93dcb9fd6b', 'TRO', 'af606e56-0707-4b11-9bdf-687e94f7e5f1', '{ "displayName" : "DCU Treat Official", "shortCode" : "a2", "bulk" : true }'),
       ('ec7a4214-abd2-4c77-a954-0d39522dadfe','DTEN', 'af606e56-0707-4b11-9bdf-687e94f7e5f1', '{ "displayName" : "DCU Number 10", "shortCode" : "a3", "bulk" : false }'),
       ('c99bd299-641a-485f-bc94-55fc9b8bfc3f', 'IMCB', 'af606e56-0707-4b11-9bdf-687e94f7e5f1', '{ "displayName" : "UKVI B Ref", "shortCode" : "b1", "bulk" : true }'),
       ('46e7c79c-d45e-4a45-9dfd-313917011b14', 'IMCM', 'af606e56-0707-4b11-9bdf-687e94f7e5f1', '{ "displayName" : "UKVI M Ref", "shortCode" : "b2", "bulk" : true }'),
       ('9a26741d-83ae-427f-a84f-204a459abbc9', 'UTEN', 'af606e56-0707-4b11-9bdf-687e94f7e5f1', '{ "displayName" : "UKVI Number 10", "shortCode" : "b3", "bulk" : true }');

INSERT INTO entity_relation(entity_uuid, parent_entity_uuid) -- link case types to units
VALUES ('28c09fd9-c6cc-4c94-8906-2876c6b99388', '7dbd7eed-df69-4601-a88d-00f569021704' ),
       ('d7c4bb49-9ff2-4193-a704-6e93dcb9fd6b', '7dbd7eed-df69-4601-a88d-00f569021704' ),
       ('ec7a4214-abd2-4c77-a954-0d39522dadfe', '7dbd7eed-df69-4601-a88d-00f569021704' ),
       ('c99bd299-641a-485f-bc94-55fc9b8bfc3f', 'ffe5c6ba-be21-4f8e-9b5f-abfc7fd6f05a' ),
       ('46e7c79c-d45e-4a45-9dfd-313917011b14', 'ffe5c6ba-be21-4f8e-9b5f-abfc7fd6f05a' ),
       ('9a26741d-83ae-427f-a84f-204a459abbc9', 'ffe5c6ba-be21-4f8e-9b5f-abfc7fd6f05a' );

-- ########### STAGETYPES LIST
INSERT INTO entity_list (uuid, display_name, simple_name)
VALUES ('8610d872-7827-4270-a1cc-73a45ea1c13a', 'Stage Types', 'stageType');

-- TODO: all the stages
INSERT INTO entity (uuid, simple_name, entity_list_UUID, data)
VALUES ('f1fde35b-0618-4a03-842d-7495b936d1fe', 'DCU_MIN_DATA_INPUT','8610d872-7827-4270-a1cc-73a45ea1c13a', '{ "displayName" : "Data Input", "shortCode" : "a1", "sla" : "10" }');

INSERT INTO entity_relation(entity_uuid, parent_entity_uuid) -- link stage types to case types
VALUES ('f1fde35b-0618-4a03-842d-7495b936d1fe', '28c09fd9-c6cc-4c94-8906-2876c6b99388' );

-- ########### FORMS LIST
INSERT INTO entity_list (uuid, display_name, simple_name)
VALUES ('6016e4cd-9f34-49fe-ac84-cd4dca135ff9', 'Forms', 'form');

-- TODO: all the forms
INSERT INTO entity (uuid, simple_name, entity_list_UUID, data)
VALUES ('afa670fa-8048-4207-a0f6-35e856ffb70d', 'DCU_CORRESPONDENCE_DETAILS','6016e4cd-9f34-49fe-ac84-cd4dca135ff9', '{ "title" : "Record Correspondence Details", "defaultActionLabel" : "Continue"}');

INSERT INTO entity_relation(entity_uuid, parent_entity_uuid) -- link form to Stage
VALUES ('afa670fa-8048-4207-a0f6-35e856ffb70d', 'f1fde35b-0618-4a03-842d-7495b936d1fe' );

-- ########### SUMMARIES LIST
INSERT INTO entity_list (uuid, display_name, simple_name)
VALUES ('00663cff-af7d-4744-abd3-5b88175457e6', 'Summaries', 'summary');

-- TODO: all the summaries
INSERT INTO entity (uuid, simple_name, entity_list_UUID)
VALUES ('336966fe-8e42-47f6-996d-3cef06c1a407', 'MIN_SUMMARY','00663cff-af7d-4744-abd3-5b88175457e6');

INSERT INTO entity_relation(entity_uuid, parent_entity_uuid) -- link form to CaseType
VALUES ('336966fe-8e42-47f6-996d-3cef06c1a407', '28c09fd9-c6cc-4c94-8906-2876c6b99388' );

-- ########### FIELDS LIST
INSERT INTO entity_list (uuid, display_name, simple_name)
VALUES ('40537d93-906e-41f8-b41d-0a79af48ce3e', 'Fields', 'field');

INSERT INTO entity (uuid, simple_name, entity_list_UUID, data)
VALUES ('ede4aa5d-80d5-4703-aeed-82167d927ad7', 'OriginalChannel','40537d93-906e-41f8-b41d-0a79af48ce3e', '{ "component" : "radio",  "validation" : [ "required" ], "props" : { "name" : "OriginalChannel", "label" : "How was the correspondence received?", "choices" : [ {  "label" : "Email", "value" : "EMAIL" }, { "label" : "Post", "value" : "POST" }, { "label" : "Phone", "value" : "PHONE" }, { "label" : "No. 10", "value" : "NO10" } ] } }'),
       ('03548dc4-76bb-4ac8-8992-b555fd59fa0a', 'DateOfCorrespondence','40537d93-906e-41f8-b41d-0a79af48ce3e', '{ "component" : "date", "validation" : [ "required" ], "props" : { "name" : "DateOfCorrespondence", "label" : "When was the correspondence sent?" } }'),
       ('1cb5ee23-b82d-448d-8574-00421841acdc', 'DateReceived','40537d93-906e-41f8-b41d-0a79af48ce3e', '{ "component" : "date", "validation" : [ "required" ], "props" : { "name" : "DateReceived", "label" : "When was the correspondence received?" } }'),
       ('157a00e6-3b96-4a12-9b34-f73c328c332c', 'CopyNumberTen','40537d93-906e-41f8-b41d-0a79af48ce3e', '{ "component" : "checkbox", "validation" : [ ], "props" : { "name" : "CopyNumberTen", "label" : "", "choices" : [ { "label" : "Send a copy to Number 10?" , "value" : "TRUE" } ] } }');


INSERT INTO entity_relation(entity_uuid, parent_entity_uuid) -- link field to form
VALUES ('ede4aa5d-80d5-4703-aeed-82167d927ad7', 'afa670fa-8048-4207-a0f6-35e856ffb70d' ),
       ('03548dc4-76bb-4ac8-8992-b555fd59fa0a', 'afa670fa-8048-4207-a0f6-35e856ffb70d' ),
       ('1cb5ee23-b82d-448d-8574-00421841acdc', 'afa670fa-8048-4207-a0f6-35e856ffb70d' ),
       ('157a00e6-3b96-4a12-9b34-f73c328c332c', 'afa670fa-8048-4207-a0f6-35e856ffb70d' );


INSERT INTO entity_relation(entity_uuid, parent_entity_uuid) -- link field to summary
VALUES ('1cb5ee23-b82d-448d-8574-00421841acdc', '336966fe-8e42-47f6-996d-3cef06c1a407' );