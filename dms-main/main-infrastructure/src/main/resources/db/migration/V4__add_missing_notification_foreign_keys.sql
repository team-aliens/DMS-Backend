-- #1121 notification 서비스에서 넘어온 테이블이라 user_id·school_id에 FK가 빠져 있었다
-- 유저·학교는 소프트 삭제만 하므로 ON DELETE 동작은 두지 않는다
ALTER TABLE tbl_device_token
    ADD CONSTRAINT fk_device_token_user_id FOREIGN KEY (user_id) REFERENCES tbl_user (id),
    ADD CONSTRAINT fk_device_token_school_id FOREIGN KEY (school_id) REFERENCES tbl_school (id);

ALTER TABLE tbl_notification_of_user
    ADD CONSTRAINT fk_notification_of_user_user_id FOREIGN KEY (user_id) REFERENCES tbl_user (id);

-- PostgreSQL은 FK 컬럼에 인덱스를 자동으로 만들지 않는다. device_token.user_id는 UNIQUE 제약이 인덱스를 겸한다
CREATE INDEX idx_device_token_school_id
    ON tbl_device_token (school_id);

CREATE INDEX idx_notification_of_user_user_id
    ON tbl_notification_of_user (user_id);
