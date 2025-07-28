CREATE TABLE experiment_post_keywords_log (
    experiment_post_keywords_log_id CHAR(13) NOT NULL PRIMARY KEY,
    member_id CHAR(13) NOT NULL,
    response TEXT NOT NULL,
    created_at TIMESTAMP NOT NULL,
    CONSTRAINT fk_experiment_post_keywords_log_member
        FOREIGN KEY (member_id) REFERENCES member (member_id)
) COLLATE = utf8mb4_unicode_ci;

CREATE INDEX idx_experiment_keywords_log ON experiment_post_keywords_log (member_id, created_at);
