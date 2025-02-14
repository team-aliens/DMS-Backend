create table tbl_vote
(
    created_at          datetime(6) not null,
    voted_at            datetime(6)    null,
    id                  binary(16)  not null
        primary key,
    selected_option_id  binary(16)  null,
    selected_student_id binary(16)  null,
    student_id          binary(16)  not null,
    voting_topic_id     binary(16)  not null,
    foreign key (voting_topic_id) references tbl_voting_topic (id),
    foreign key (student_id) references tbl_student (id),
    foreign key (selected_student_id) references tbl_student (id),
    foreign key (selected_option_id) references tbl_voting_option (id)
);

create table tbl_voting_option
(
    created_at      datetime(6)  not null,
    id              binary(16)   not null
        primary key,
    option_name     varchar(255) null,
    voting_topic_id binary(16)   not null,
    foreign key (voting_topic_id) references tbl_voting_topic (id)
);

create table tbl_voting_topic
(
    id          binary(16) not null primary key,
    manager_id  binary(16)   not null,
    start_time  datetime(6)  not null,
    end_time    datetime(6)  not null,
    topic_name  varchar(255) not null,
    vote_type   varchar(20)  not null,
    description varchar(255) not null,
    foreign key (manager_id) references tbl_manager (user_id)
);
