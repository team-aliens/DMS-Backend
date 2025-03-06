create table tbl_voting_option
(
    created_at      datetime(6)  not null,
    id              binary(16)   not null
        primary key,
    option_name     varchar(255) null,
    voting_topic_id binary(16)   not null,
    foreign key (voting_topic_id) references tbl_voting_topic (id)
);