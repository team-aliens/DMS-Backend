-- 새벽자습을 날짜별로 골라 신청(예: 월·수)할 수 있도록 날짜 목록 컬럼 추가
ALTER TABLE tbl_daybreak_study_application
    ADD COLUMN study_dates date[];

-- 기존 신청은 구간 신청이었으므로 start_date~end_date의 모든 날짜로 채운다
UPDATE tbl_daybreak_study_application
SET study_dates = ARRAY(
        SELECT d::date
        FROM generate_series(start_date, end_date, interval '1 day') AS d
        ORDER BY d
    );

ALTER TABLE tbl_daybreak_study_application
    ALTER COLUMN study_dates SET NOT NULL;
