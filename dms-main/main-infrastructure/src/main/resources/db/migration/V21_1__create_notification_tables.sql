create table tbl_device_token
(
    id        binary(16)   not null primary key,
    user_id   binary(16)   not null,
    school_id binary(16)   not null,
    token     varchar(500) not null
);

create table tbl_notification_of_user
(
    id              binary(16)   not null primary key,
    user_id         binary(16)   not null,
    topic           varchar(20)  not null,
    link_identifier varchar(500) null,
    title           varchar(500) not null,
    content         varchar(500) not null,
    created_at      datetime(6)  not null,
    is_read         bit(1)       not null default false
);

create table tbl_topic_subscription
(
    device_token_id binary(16)  not null,
    topic           varchar(20) not null,
    is_subscribed   bit(1)      not null,
    primary key (device_token_id, topic),
    foreign key (device_token_id) references tbl_device_token (id)
);
