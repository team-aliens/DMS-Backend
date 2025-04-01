ALTER TABLE tbl_voting_option
    ADD CONSTRAINT fk_voting_topic
        FOREIGN KEY (voting_topic_id) REFERENCES tbl_voting_topic (id)
            ON DELETE CASCADE;

ALTER TABLE tbl_vote
    ADD CONSTRAINT fk_vote_topic
        FOREIGN KEY (voting_topic_id) REFERENCES tbl_voting_topic (id)
            ON DELETE CASCADE,
    ADD CONSTRAINT fk_vote_student
        FOREIGN KEY (student_id) REFERENCES tbl_student (id)
            ON DELETE CASCADE,
    ADD CONSTRAINT fk_vote_selected_option
        FOREIGN KEY (selected_option_id) REFERENCES tbl_voting_option (id)
            ON DELETE CASCADE,
    ADD CONSTRAINT fk_vote_selected_student
        FOREIGN KEY (selected_student_id) REFERENCES tbl_student (id)
            ON DELETE CASCADE;
