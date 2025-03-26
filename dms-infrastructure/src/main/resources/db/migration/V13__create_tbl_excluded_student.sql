create table tbl_excluded_student
(
    student_id binary(16) not null primary key,
    school_id  binary(16) not null,
    foreign key (student_id) references tbl_student (id),
    foreign key (school_id) references tbl_school (id)
);
