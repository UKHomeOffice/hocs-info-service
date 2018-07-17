DROP TABLE IF EXISTS tenant;

CREATE TABLE IF NOT EXISTS tenant
(
  id           BIGSERIAL PRIMARY KEY,
  display_name TEXT NOT NULL

);

DROP TABLE IF EXISTS holiday;

CREATE TABLE IF NOT EXISTS holiday
(
  id   BIGSERIAL PRIMARY KEY,
  date timestamp NOT NULL

);

CREATE TABLE IF NOT EXISTS tenants_holidays
(
  tenant_id  INT NOT NULL,
  holiday_id INT NOT NULL,

  PRIMARY KEY (tenant_id, holiday_id),
  CONSTRAINT fk_user_id FOREIGN KEY (tenant_id) REFERENCES tenant (id),
  CONSTRAINT fk_team_id FOREIGN KEY (holiday_id) REFERENCES holiday (id)
);

DROP TABLE IF EXISTS case_type;

CREATE TABLE IF NOT EXISTS case_type
(
  id           BIGSERIAL PRIMARY KEY,
  display_name TEXT      NOT NULL,
  type         TEXT      NOT NULL,
  tenant_id    BIGSERIAL NOT NULL,

  CONSTRAINT fk_case_type_id FOREIGN KEY (tenant_id) REFERENCES tenant (id)
);

DROP TABLE IF EXISTS sla;

CREATE TABLE IF NOT EXISTS sla
(
  id           BIGSERIAL PRIMARY KEY,
  type         TEXT      NOT NULL,
  value        int       NOT NULL,
  case_type_id BIGSERIAL NOT NULL,

  CONSTRAINT fk_sla_id FOREIGN KEY (case_type_id) REFERENCES case_type (id)
);

DROP TABLE IF EXISTS unit;

CREATE TABLE IF NOT EXISTS unit
(
  id           BIGSERIAL PRIMARY KEY,
  display_name TEXT      NOT NULL,
  tenant_id    BIGSERIAL NOT NULL,

  CONSTRAINT fk_unit_id FOREIGN KEY (tenant_id) REFERENCES tenant (id)
);

DROP TABLE IF EXISTS team;

CREATE TABLE IF NOT EXISTS team
(
  id           BIGSERIAL PRIMARY KEY,
  display_name TEXT      NOT NULL,
  unit_id      BIGSERIAL NOT NULL,

  CONSTRAINT fk_team_id FOREIGN KEY (unit_id) REFERENCES unit (id)
);

DROP TABLE IF EXISTS parent_topic;

CREATE TABLE IF NOT EXISTS parent_topic
(
  id           BIGSERIAL PRIMARY KEY,
  display_name TEXT      NOT NULL,
  tenant_id    BIGSERIAL NOT NULL,

   CONSTRAINT fk_parent_topic_id FOREIGN KEY (tenant_id) REFERENCES tenant (id)
 );

DROP TABLE IF EXISTS minister;

CREATE TABLE IF NOT EXISTS minister
(
  id           BIGSERIAL PRIMARY KEY,
  display_name TEXT NOT NULL
);


 DROP TABLE IF EXISTS topic;

 CREATE TABLE IF NOT EXISTS topic
 (
   id              BIGSERIAL PRIMARY KEY,
   display_name    TEXT      NOT NULL,
   parent_topic_id BIGSERIAL NOT NULL,
   team_id         BIGSERIAL NOT NULL,
   minister_id     BIGSERIAL NOT NULL,

   CONSTRAINT fk_parent_topic_id FOREIGN KEY (parent_topic_id) REFERENCES parent_topic (id),
   CONSTRAINT fk_team_id FOREIGN KEY (team_id) REFERENCES unit (id),
   CONSTRAINT fk_minister_id FOREIGN KEY (minister_id) REFERENCES minister (id)
 );

 DROP TABLE IF EXISTS standard_lines;

 CREATE TABLE IF NOT EXISTS standard_lines
 (
   id           BIGSERIAL PRIMARY KEY,
   display_name TEXT      NOT NULL,
   s3_link      TEXT      NOT NULL,
   topic_id     BIGSERIAL NOT NULL,

   CONSTRAINT fk_standard_lines_id FOREIGN KEY (topic_id) REFERENCES topic (id)
 );

 DROP TABLE IF EXISTS templates;

 CREATE TABLE IF NOT EXISTS templates
 (
   id           BIGSERIAL PRIMARY KEY,
   display_name TEXT      NOT NULL,
   s3_link      TEXT      NOT NULL,
   topic_id     BIGSERIAL NOT NULL,

   CONSTRAINT fk_templates_id FOREIGN KEY (topic_id) REFERENCES topic (id)
 );

 DROP TABLE IF EXISTS member;

 CREATE TABLE IF NOT EXISTS member
 (
   id           BIGSERIAL PRIMARY KEY,
   display_name TEXT NOT NULL,
   team_id BIGSERIAL NOT NULL,

   CONSTRAINT fk_member_id FOREIGN KEY (team_id) REFERENCES team (id)
 );

