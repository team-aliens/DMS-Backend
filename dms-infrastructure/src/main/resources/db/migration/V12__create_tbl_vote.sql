CREATE TABLE tbl_vote
(
    created_at          DATETIME(6) NOT NULL,
    voted_at            DATETIME(6) NULL,
    id                  BINARY(16) NOT NULL PRIMARY KEY,
    selected_option_id  BINARY(16) NULL,
    selected_student_id BINARY(16) NULL,
    student_id          BINARY(16) NOT NULL,
    voting_topic_id     BINARY(16) NOT NULL,
    FOREIGN KEY (voting_topic_id) REFERENCES tbl_voting_topic (id),
    FOREIGN KEY (student_id) REFERENCES tbl_student (id),
    FOREIGN KEY (selected_student_id) REFERENCES tbl_student (id),
    FOREIGN KEY (selected_option_id) REFERENCES tbl_voting_option (id)
);
