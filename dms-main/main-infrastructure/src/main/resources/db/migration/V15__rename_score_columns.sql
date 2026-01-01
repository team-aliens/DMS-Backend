alter table tbl_volunteer
    change column score max_score int not null,
    change column optional_score min_score int not null;
