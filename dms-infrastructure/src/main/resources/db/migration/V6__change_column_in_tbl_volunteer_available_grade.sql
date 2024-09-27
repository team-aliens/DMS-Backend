ALTER TABLE tbl_volunteer
    MODIFY COLUMN available_grade VARCHAR(16);

UPDATE tbl_volunteer
    set available_grade = REPLACE(available_grade, "_", "_AND_")
    where available_grade LIKE "%_%";