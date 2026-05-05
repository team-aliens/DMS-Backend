CREATE TABLE tbl_teacher (
    user_id     BINARY(16)   NOT NULL primary key,
    name        VARCHAR(10)  NOT NULL,
    grade       TINYINT UNSIGNED NULL,
    FOREIGN KEY (user_id) REFERENCES tbl_user (id)
);

ALTER TABLE tbl_user
MODIFY authority VARCHAR(20) NOT NULL;