create table tbl_outbox
(
    id             binary(16)   not null primary key,
    aggregate_type varchar(100) not null,
    event_type     varchar(100) not null,
    payload        text         not null,
    status         varchar(20)  not null,
    retry_count    int          not null default 0,
    created_at     datetime(6)  not null,
    processed_at   datetime     null,
    index idx_outbox_status (status),
    index idx_outbox_created_at (created_at)
);
