create table tbl_excluded_student
(
    student_id binary(16) not null primary key,
    foreign key (student_id) references tbl_student (id)
);