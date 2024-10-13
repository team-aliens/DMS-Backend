alter table tbl_outing_application
    add column is_approved bit(1) default false not null,
    add column is_comeback bit(1) default false not null;
