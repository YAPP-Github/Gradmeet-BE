ALTER TABLE experiment_post_keywords_log DROP FOREIGN KEY fk_experiment_post_keywords_log_member;

DROP INDEX idx_experiment_keywords_log ON experiment_post_keywords_log;

ALTER TABLE experiment_post_keywords_log
ADD CONSTRAINT fk_experiment_post_keywords_log_member FOREIGN KEY (member_id) REFERENCES member (member_id);
