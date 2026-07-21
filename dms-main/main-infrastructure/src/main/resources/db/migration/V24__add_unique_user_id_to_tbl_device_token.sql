ALTER TABLE tbl_device_token
    ADD CONSTRAINT uk_device_token_user_id UNIQUE (user_id);
