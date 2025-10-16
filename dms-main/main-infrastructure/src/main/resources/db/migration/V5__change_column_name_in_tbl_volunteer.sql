ALTER TABLE tbl_volunteer
    CHANGE COLUMN sex_condition available_sex VARCHAR(6) NOT NULL,
    CHANGE COLUMN grade_condition available_grade VARCHAR(14) NOT NULL;

