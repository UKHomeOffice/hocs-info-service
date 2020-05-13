
DROP TABLE IF EXISTS info.priority_policy;
CREATE TABLE info.priority_policy
(
    id                    BIGSERIAL PRIMARY KEY,
    policy_type           VARCHAR(50) NOT NULL,
    applicable_case_type  VARCHAR(50) NOT NULL,
    config                JSONB NOT NULL DEFAULT '{}'
);
