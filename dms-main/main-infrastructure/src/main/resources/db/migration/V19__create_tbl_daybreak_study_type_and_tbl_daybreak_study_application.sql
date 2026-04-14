-- 새벽 자습 타입 테이블
CREATE TABLE tbl_daybreak_study_type (
     id          BINARY(16)   NOT NULL primary key,
     school_id   BINARY(16)   NOT NULL,
     name        VARCHAR(20)  NOT NULL unique,
     FOREIGN KEY (school_id) REFERENCES tbl_school (id)
);

-- 새벽 자습 신청 테이블
CREATE TABLE tbl_daybreak_study_application (
    id           BINARY(16)    NOT NULL primary key,
    reason       VARCHAR(200) NOT NULL,
    status       VARCHAR(20)   NOT NULL,
    start_date   DATE          NOT NULL,
    end_date     DATE          NOT NULL,
    student_id   BINARY(16)    NOT NULL,
    teacher_id   BINARY(16)    NOT NULL,
    school_id    BINARY(16)    NOT NULL,
    type_id      BINARY(16)    NOT NULL,
    created_at   DATETIME(6)   NOT NULL,
    FOREIGN KEY (student_id) REFERENCES tbl_student (id),
    FOREIGN KEY (teacher_id) REFERENCES tbl_teacher (user_id),
    FOREIGN KEY (school_id) REFERENCES tbl_school (id),
    FOREIGN KEY (type_id) REFERENCES tbl_daybreak_study_type (id)
);



