-- 새벽 자습 신청 되돌리기(revert)를 위해 직전 상태를 보관하는 컬럼 추가
alter table tbl_daybreak_study_application
    add column previous_status varchar(20) null after status;
