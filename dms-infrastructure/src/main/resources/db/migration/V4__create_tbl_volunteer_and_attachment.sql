create table tbl_volunteer
(
    id binary(16)   not null primary key,
    created_at      datetime(6)  not null,
    content         varchar(255) not null,
    grade_condition varchar(14)  not null,
    max_applicants  int unsigned not null,
    name            varchar(255) not null,
    optional_score  int          not null,
    score           int          not null,
    sex_condition   varchar(6)   not null,
    school_id       binary(16)   not null,
    foreign key (school_id) references tbl_school (id)
);

create table tbl_volunteer_application
(
    id binary(16)  not null primary key,
    created_at   datetime(6) not null,
    approved     bit         not null,
    student_id   binary(16)  not null,
    volunteer_id binary(16)  not null,
    foreign key (volunteer_id) references tbl_volunteer (id),
    foreign key (student_id) references tbl_student (id)
);
