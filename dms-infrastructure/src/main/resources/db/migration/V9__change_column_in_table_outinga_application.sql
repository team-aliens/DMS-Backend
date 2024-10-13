alter table tbl_outing_application
drop column status,
add column is_approved bit(1) not null default b'0',
add column is_comeback bit(1) not null default b'0';
