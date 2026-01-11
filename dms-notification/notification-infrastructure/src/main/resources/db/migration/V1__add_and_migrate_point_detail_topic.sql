alter table tbl_notification_of_user
    add column point_detail_topic varchar(20) null;

update tbl_notification_of_user
set point_detail_topic =
        case
            when title like '%상점%' then 'PLUS'
            when title like '%벌점%' then 'MINUS'
            else null
            end
where topic = 'POINT' and point_detail_topic is null;