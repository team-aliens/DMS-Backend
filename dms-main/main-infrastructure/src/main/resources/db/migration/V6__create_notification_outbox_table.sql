-- #1145 알림 발송 요청을 비즈니스 트랜잭션과 같이 커밋해 두고, 서버 다운·FCM 실패로 못 보낸 건을 스케줄러가 다시 보낸다
-- 발송이 끝난 행은 지우고, 재시도를 다 쓴 행만 FAILED 로 남긴다
CREATE TABLE tbl_notification_outbox
(
    id                 uuid                           NOT NULL PRIMARY KEY,
    user_ids           uuid[]                         NOT NULL,
    school_id          uuid                           NOT NULL,
    topic              varchar(30)                    NOT NULL,
    point_detail_topic varchar(20),
    link_identifier    varchar(500),
    title              varchar(500)                   NOT NULL,
    content            varchar(500)                   NOT NULL,
    thread_id          varchar(500)                   NOT NULL,
    is_save_required   boolean                        NOT NULL,
    status             varchar(10)                    NOT NULL,
    retry_count        integer                        NOT NULL,
    created_at         timestamp(6) WITHOUT TIME ZONE NOT NULL,

    CONSTRAINT tbl_notification_outbox_status_check CHECK (status IN ('PENDING', 'FAILED')),
    CONSTRAINT tbl_notification_outbox_point_detail_topic_check CHECK (point_detail_topic IN ('BONUS', 'MINUS')),
    CONSTRAINT tbl_notification_outbox_topic_check CHECK (topic IN ('NOTICE', 'POINT', 'DAYBREAK_STUDY_APPLICATION'))
);
