create table tbl_bug_report (
    id binary(16) primary key,
    student_id binary(16) not null,
    content varchar(300) not null,
    development_area varchar(7) not null,
    created_at timestamp not null,
    foreign key (student_id) references tbl_student(id)
);

create table tbl_bug_attachment (
    bug_report_id binary(16) not null,
    attachment_url varchar(300) not null,
    foreign key (bug_report_id) references tbl_bug_report(id)
);