create table tbl_volunteer_score
(
    id              binary(16)   not null primary key,
    created_at      datetime(6)  not null,
    application_id  binary(16)   not null,
    assign_score    int          not null,
    foreign key (application_id) references tbl_volunteer_application(id)
);
