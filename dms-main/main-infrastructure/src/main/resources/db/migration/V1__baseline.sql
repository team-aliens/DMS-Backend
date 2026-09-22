-- 이슈 #1106 PostgreSQL 마이그레이션 baseline
-- V1~V28(MySQL 방언)을 대체합니다. 새 PostgreSQL 인스턴스에 전체 스키마를 한 번에 만듭니다.
-- 생성 근거: 엔티티에서 Hibernate가 생성한 DDL + 운영 MySQL 덤프(2026-09-21)의 인덱스/제약/nullability 대조

-- ===== 애플리케이션 테이블 =====
CREATE TABLE tbl_available_feature (
    daybreak_service boolean NOT NULL,
    meal_service boolean NOT NULL,
    notice_service boolean NOT NULL,
    point_service boolean NOT NULL,
    remain_service boolean NOT NULL,
    school_id uuid NOT NULL
);
CREATE TABLE tbl_bug_attachment (
    bug_report_id uuid NOT NULL,
    attachment_url character varying(300) NOT NULL
);
CREATE TABLE tbl_bug_report (
    development_area character varying(7) NOT NULL,
    created_at timestamp(6) without time zone NOT NULL,
    id uuid NOT NULL,
    student_id uuid NOT NULL,
    content character varying(300) NOT NULL,
    CONSTRAINT tbl_bug_report_development_area_check CHECK (((development_area)::text = ANY ((ARRAY['IOS'::character varying, 'ANDROID'::character varying])::text[])))
);
CREATE TABLE tbl_daybreak_study_application (
    end_date date NOT NULL,
    start_date date NOT NULL,
    created_at timestamp(6) without time zone NOT NULL,
    id uuid NOT NULL,
    school_id uuid NOT NULL,
    student_id uuid NOT NULL,
    teacher_id uuid NOT NULL,
    type_id uuid NOT NULL,
    previous_status character varying(20),
    status character varying(20) NOT NULL,
    reason character varying(200) NOT NULL,
    CONSTRAINT tbl_daybreak_study_application_previous_status_check CHECK (((previous_status)::text = ANY ((ARRAY['PENDING'::character varying, 'FIRST_APPROVED'::character varying, 'SECOND_APPROVED'::character varying, 'REJECTED'::character varying, 'EXPIRED'::character varying])::text[]))),
    CONSTRAINT tbl_daybreak_study_application_status_check CHECK (((status)::text = ANY ((ARRAY['PENDING'::character varying, 'FIRST_APPROVED'::character varying, 'SECOND_APPROVED'::character varying, 'REJECTED'::character varying, 'EXPIRED'::character varying])::text[])))
);
CREATE TABLE tbl_daybreak_study_type (
    id uuid NOT NULL,
    school_id uuid NOT NULL,
    name character varying(20) NOT NULL
);
CREATE TABLE tbl_device_token (
    id uuid NOT NULL,
    school_id uuid NOT NULL,
    user_id uuid NOT NULL,
    token character varying(500) NOT NULL
);
CREATE TABLE tbl_excluded_student (
    school_id uuid NOT NULL,
    student_id uuid NOT NULL
);
CREATE TABLE tbl_manager (
    name character varying(10) NOT NULL,
    user_id uuid NOT NULL,
    profile_image_url character varying(255) NOT NULL
);
CREATE TABLE tbl_meal (
    meal_date date NOT NULL,
    school_id uuid NOT NULL,
    breakfast character varying(255),
    dinner character varying(255),
    lunch character varying(255)
);
CREATE TABLE tbl_notice (
    created_at timestamp(6) without time zone NOT NULL,
    updated_at timestamp(6) without time zone NOT NULL,
    id uuid NOT NULL,
    manager_id uuid NOT NULL,
    title character varying(100) NOT NULL,
    content character varying(1000) NOT NULL
);
CREATE TABLE tbl_notification_of_user (
    is_read boolean DEFAULT false NOT NULL,
    created_at timestamp(6) without time zone NOT NULL,
    id uuid NOT NULL,
    user_id uuid NOT NULL,
    point_detail_topic character varying(20),
    topic character varying(30) NOT NULL,
    content character varying(500) NOT NULL,
    link_identifier character varying(500),
    title character varying(500) NOT NULL,
    CONSTRAINT tbl_notification_of_user_point_detail_topic_check CHECK (((point_detail_topic)::text = ANY ((ARRAY['BONUS'::character varying, 'MINUS'::character varying])::text[]))),
    CONSTRAINT tbl_notification_of_user_topic_check CHECK (((topic)::text = ANY ((ARRAY['NOTICE'::character varying, 'POINT'::character varying, 'DAYBREAK_STUDY_APPLICATION'::character varying])::text[])))
);
CREATE TABLE tbl_phrase (
    standard integer NOT NULL,
    type character varying(5) NOT NULL,
    id uuid NOT NULL,
    content character varying(30) NOT NULL,
    CONSTRAINT tbl_phrase_type_check CHECK (((type)::text = ANY ((ARRAY['BONUS'::character varying, 'MINUS'::character varying])::text[])))
);
CREATE TABLE tbl_point_filter (
    max_point integer NOT NULL,
    min_point integer NOT NULL,
    point_type character varying(5) NOT NULL,
    name character varying(10) NOT NULL,
    id uuid NOT NULL,
    school_id uuid NOT NULL,
    CONSTRAINT tbl_point_filter_point_type_check CHECK (((point_type)::text = ANY ((ARRAY['BONUS'::character varying, 'MINUS'::character varying])::text[])))
);
CREATE TABLE tbl_point_history (
    bonus_total integer NOT NULL,
    is_cancel boolean NOT NULL,
    minus_total integer NOT NULL,
    point_score integer NOT NULL,
    point_type character varying(5) NOT NULL,
    student_gcn character varying(5) NOT NULL,
    created_at timestamp(6) without time zone NOT NULL,
    id uuid NOT NULL,
    school_id uuid NOT NULL,
    point_name character varying(30) NOT NULL,
    student_name character varying(30) NOT NULL,
    CONSTRAINT tbl_point_history_point_type_check CHECK (((point_type)::text = ANY ((ARRAY['BONUS'::character varying, 'MINUS'::character varying])::text[])))
);
CREATE TABLE tbl_point_option (
    score integer NOT NULL,
    type character varying(5) NOT NULL,
    created_at timestamp(6) without time zone,
    id uuid NOT NULL,
    school_id uuid NOT NULL,
    name character varying(30) NOT NULL,
    CONSTRAINT tbl_point_option_type_check CHECK (((type)::text = ANY ((ARRAY['BONUS'::character varying, 'MINUS'::character varying])::text[])))
);
CREATE TABLE tbl_remain_available_time (
    end_time time(6) without time zone NOT NULL,
    start_time time(6) without time zone NOT NULL,
    end_day_of_week character varying(10) NOT NULL,
    start_day_of_week character varying(10) NOT NULL,
    school_id uuid NOT NULL,
    CONSTRAINT tbl_remain_available_time_end_day_of_week_check CHECK (((end_day_of_week)::text = ANY ((ARRAY['MONDAY'::character varying, 'TUESDAY'::character varying, 'WEDNESDAY'::character varying, 'THURSDAY'::character varying, 'FRIDAY'::character varying, 'SATURDAY'::character varying, 'SUNDAY'::character varying])::text[]))),
    CONSTRAINT tbl_remain_available_time_start_day_of_week_check CHECK (((start_day_of_week)::text = ANY ((ARRAY['MONDAY'::character varying, 'TUESDAY'::character varying, 'WEDNESDAY'::character varying, 'THURSDAY'::character varying, 'FRIDAY'::character varying, 'SATURDAY'::character varying, 'SUNDAY'::character varying])::text[])))
);
CREATE TABLE tbl_remain_option (
    id uuid NOT NULL,
    school_id uuid NOT NULL,
    title character varying(100) NOT NULL,
    description character varying(255) NOT NULL
);
CREATE TABLE tbl_remain_status (
    created_at timestamp(6) without time zone NOT NULL,
    remain_option_id uuid NOT NULL,
    student_id uuid NOT NULL
);
CREATE TABLE tbl_room (
    number character varying(4) NOT NULL,
    id uuid NOT NULL,
    school_id uuid NOT NULL
);
CREATE TABLE tbl_school (
    contract_ended_at date,
    contract_started_at date NOT NULL,
    code character varying(8) NOT NULL,
    id uuid NOT NULL,
    name character varying(20) NOT NULL,
    answer character varying(100) NOT NULL,
    question character varying(100) NOT NULL,
    address character varying(255) NOT NULL
);
CREATE TABLE tbl_student (
    class_room integer NOT NULL,
    grade integer NOT NULL,
    number integer NOT NULL,
    room_location character varying(1) NOT NULL,
    sex character varying(6) NOT NULL,
    deleted_at timestamp(6) without time zone,
    name character varying(10) NOT NULL,
    id uuid NOT NULL,
    room_id uuid NOT NULL,
    user_id uuid,
    profile_image_url character varying(500) NOT NULL,
    CONSTRAINT tbl_student_sex_check CHECK (((sex)::text = ANY ((ARRAY['MALE'::character varying, 'FEMALE'::character varying, 'ALL'::character varying])::text[])))
);
CREATE TABLE tbl_student_tag (
    created_at timestamp(6) without time zone NOT NULL,
    student_id uuid NOT NULL,
    tag_id uuid NOT NULL
);
CREATE TABLE tbl_tag (
    color character varying(7) NOT NULL,
    name character varying(10) NOT NULL,
    id uuid NOT NULL,
    school_id uuid NOT NULL
);
CREATE TABLE tbl_teacher (
    grade integer,
    name character varying(10) NOT NULL,
    user_id uuid NOT NULL
);
CREATE TABLE tbl_topic_subscription (
    is_subscribed boolean NOT NULL,
    device_token_id uuid NOT NULL,
    topic character varying(30) NOT NULL,
    CONSTRAINT tbl_topic_subscription_topic_check CHECK (((topic)::text = ANY ((ARRAY['NOTICE'::character varying, 'POINT'::character varying, 'DAYBREAK_STUDY_APPLICATION'::character varying])::text[])))
);
CREATE TABLE tbl_user (
    created_at timestamp(6) without time zone NOT NULL,
    deleted_at timestamp(6) without time zone,
    id uuid NOT NULL,
    school_id uuid NOT NULL,
    account_id character varying(20) NOT NULL,
    authority character varying(20) NOT NULL,
    password character varying(60) NOT NULL,
    email character varying(255) NOT NULL,
    CONSTRAINT tbl_user_authority_check CHECK (((authority)::text = ANY ((ARRAY['STUDENT'::character varying, 'MANAGER'::character varying, 'GENERAL_TEACHER'::character varying, 'HEAD_TEACHER'::character varying])::text[])))
);
CREATE TABLE tbl_vote (
    created_at timestamp(6) without time zone NOT NULL,
    voted_at timestamp(6) without time zone,
    id uuid NOT NULL,
    selected_option_id uuid,
    selected_student_id uuid,
    student_id uuid NOT NULL,
    voting_topic_id uuid NOT NULL
);
CREATE TABLE tbl_voting_option (
    created_at timestamp(6) without time zone NOT NULL,
    id uuid NOT NULL,
    voting_topic_id uuid NOT NULL,
    option_name character varying(255)
);
CREATE TABLE tbl_voting_topic (
    end_time timestamp(6) without time zone NOT NULL,
    start_time timestamp(6) without time zone NOT NULL,
    id uuid NOT NULL,
    manager_id uuid NOT NULL,
    vote_type character varying(20) NOT NULL,
    description character varying(255) NOT NULL,
    topic_name character varying(255) NOT NULL,
    CONSTRAINT tbl_voting_topic_vote_type_check CHECK (((vote_type)::text = ANY ((ARRAY['OPTION_VOTE'::character varying, 'STUDENT_VOTE'::character varying, 'MODEL_STUDENT_VOTE'::character varying, 'APPROVAL_VOTE'::character varying])::text[])))
);
ALTER TABLE ONLY tbl_available_feature
    ADD CONSTRAINT tbl_available_feature_pkey PRIMARY KEY (school_id);
ALTER TABLE ONLY tbl_bug_report
    ADD CONSTRAINT tbl_bug_report_pkey PRIMARY KEY (id);
ALTER TABLE ONLY tbl_daybreak_study_application
    ADD CONSTRAINT tbl_daybreak_study_application_pkey PRIMARY KEY (id);
ALTER TABLE ONLY tbl_daybreak_study_type
    ADD CONSTRAINT tbl_daybreak_study_type_pkey PRIMARY KEY (id);
ALTER TABLE ONLY tbl_device_token
    ADD CONSTRAINT tbl_device_token_pkey PRIMARY KEY (id);
ALTER TABLE ONLY tbl_excluded_student
    ADD CONSTRAINT tbl_excluded_student_pkey PRIMARY KEY (student_id);
ALTER TABLE ONLY tbl_manager
    ADD CONSTRAINT tbl_manager_pkey PRIMARY KEY (user_id);
ALTER TABLE ONLY tbl_meal
    ADD CONSTRAINT tbl_meal_pkey PRIMARY KEY (meal_date, school_id);
ALTER TABLE ONLY tbl_notice
    ADD CONSTRAINT tbl_notice_pkey PRIMARY KEY (id);
ALTER TABLE ONLY tbl_notification_of_user
    ADD CONSTRAINT tbl_notification_of_user_pkey PRIMARY KEY (id);
ALTER TABLE ONLY tbl_phrase
    ADD CONSTRAINT tbl_phrase_pkey PRIMARY KEY (id);
ALTER TABLE ONLY tbl_point_filter
    ADD CONSTRAINT tbl_point_filter_pkey PRIMARY KEY (id);
ALTER TABLE ONLY tbl_point_filter
    ADD CONSTRAINT tbl_point_filter_school_id_name_key UNIQUE (school_id, name);
ALTER TABLE ONLY tbl_point_history
    ADD CONSTRAINT tbl_point_history_pkey PRIMARY KEY (id);
ALTER TABLE ONLY tbl_point_option
    ADD CONSTRAINT tbl_point_option_pkey PRIMARY KEY (id);
ALTER TABLE ONLY tbl_remain_available_time
    ADD CONSTRAINT tbl_remain_available_time_pkey PRIMARY KEY (school_id);
ALTER TABLE ONLY tbl_remain_option
    ADD CONSTRAINT tbl_remain_option_pkey PRIMARY KEY (id);
ALTER TABLE ONLY tbl_remain_status
    ADD CONSTRAINT tbl_remain_status_pkey PRIMARY KEY (student_id);
ALTER TABLE ONLY tbl_room
    ADD CONSTRAINT tbl_room_number_key UNIQUE (number);
ALTER TABLE ONLY tbl_room
    ADD CONSTRAINT tbl_room_pkey PRIMARY KEY (id);
ALTER TABLE ONLY tbl_room
    ADD CONSTRAINT tbl_room_school_id_number_key UNIQUE (school_id, number);
ALTER TABLE ONLY tbl_school
    ADD CONSTRAINT tbl_school_code_key UNIQUE (code);
ALTER TABLE ONLY tbl_school
    ADD CONSTRAINT tbl_school_name_address_key UNIQUE (name, address);
ALTER TABLE ONLY tbl_school
    ADD CONSTRAINT tbl_school_pkey PRIMARY KEY (id);
ALTER TABLE ONLY tbl_student
    ADD CONSTRAINT tbl_student_pkey PRIMARY KEY (id);
ALTER TABLE ONLY tbl_student_tag
    ADD CONSTRAINT tbl_student_tag_pkey PRIMARY KEY (student_id, tag_id);
ALTER TABLE ONLY tbl_student
    ADD CONSTRAINT tbl_student_user_id_key UNIQUE (user_id);
ALTER TABLE ONLY tbl_tag
    ADD CONSTRAINT tbl_tag_pkey PRIMARY KEY (id);
ALTER TABLE ONLY tbl_tag
    ADD CONSTRAINT tbl_tag_school_id_name_key UNIQUE (school_id, name);
ALTER TABLE ONLY tbl_teacher
    ADD CONSTRAINT tbl_teacher_pkey PRIMARY KEY (user_id);
ALTER TABLE ONLY tbl_topic_subscription
    ADD CONSTRAINT tbl_topic_subscription_pkey PRIMARY KEY (device_token_id, topic);
ALTER TABLE ONLY tbl_user
    ADD CONSTRAINT tbl_user_account_id_key UNIQUE (account_id);
ALTER TABLE ONLY tbl_user
    ADD CONSTRAINT tbl_user_email_key UNIQUE (email);
ALTER TABLE ONLY tbl_user
    ADD CONSTRAINT tbl_user_pkey PRIMARY KEY (id);
ALTER TABLE ONLY tbl_vote
    ADD CONSTRAINT tbl_vote_pkey PRIMARY KEY (id);
ALTER TABLE ONLY tbl_voting_option
    ADD CONSTRAINT tbl_voting_option_pkey PRIMARY KEY (id);
ALTER TABLE ONLY tbl_voting_topic
    ADD CONSTRAINT tbl_voting_topic_pkey PRIMARY KEY (id);
ALTER TABLE ONLY tbl_device_token
    ADD CONSTRAINT uk_device_token_user_id UNIQUE (user_id);
ALTER TABLE ONLY tbl_teacher
    ADD CONSTRAINT fk1hk3w1vvwp2qrnfn45k76sqt2 FOREIGN KEY (user_id) REFERENCES tbl_user(id);
ALTER TABLE ONLY tbl_user
    ADD CONSTRAINT fk3oq7tjci4vlmni27rqg6ormld FOREIGN KEY (school_id) REFERENCES tbl_school(id);
ALTER TABLE ONLY tbl_manager
    ADD CONSTRAINT fk441xxqq9fhaowk66l3xn3afor FOREIGN KEY (user_id) REFERENCES tbl_user(id);
ALTER TABLE ONLY tbl_excluded_student
    ADD CONSTRAINT fk4ubblkt2ggiha8mkss6225j5x FOREIGN KEY (school_id) REFERENCES tbl_school(id);
ALTER TABLE ONLY tbl_student_tag
    ADD CONSTRAINT fk5bfowdk30obsgxv3nj7pu3fj FOREIGN KEY (tag_id) REFERENCES tbl_tag(id);
ALTER TABLE ONLY tbl_daybreak_study_application
    ADD CONSTRAINT fk66c21bnp73mnb2uvr1ogmw68k FOREIGN KEY (teacher_id) REFERENCES tbl_teacher(user_id);
ALTER TABLE ONLY tbl_topic_subscription
    ADD CONSTRAINT fk71fie4frf8kikri702ck28r20 FOREIGN KEY (device_token_id) REFERENCES tbl_device_token(id);
ALTER TABLE ONLY tbl_point_option
    ADD CONSTRAINT fk78xvondf505fx1tgvlxf1qst5 FOREIGN KEY (school_id) REFERENCES tbl_school(id);
ALTER TABLE ONLY tbl_point_filter
    ADD CONSTRAINT fk7jw6tp315a5tmv6rpueno0mde FOREIGN KEY (school_id) REFERENCES tbl_school(id);
ALTER TABLE ONLY tbl_student
    ADD CONSTRAINT fk7kod8l539q1mq1uws15wkg50e FOREIGN KEY (user_id) REFERENCES tbl_user(id);
ALTER TABLE ONLY tbl_remain_status
    ADD CONSTRAINT fk9d64ru1hnev33xmior4rv9t22 FOREIGN KEY (remain_option_id) REFERENCES tbl_remain_option(id);
ALTER TABLE ONLY tbl_student_tag
    ADD CONSTRAINT fkajlqj88khe8ov76wmjxw8bed FOREIGN KEY (student_id) REFERENCES tbl_student(id);
ALTER TABLE ONLY tbl_remain_available_time
    ADD CONSTRAINT fkaqwq4ooem7d1bs9euncqya932 FOREIGN KEY (school_id) REFERENCES tbl_school(id);
ALTER TABLE ONLY tbl_tag
    ADD CONSTRAINT fkar7iiv5u6nhg7siqmr1r6lpt4 FOREIGN KEY (school_id) REFERENCES tbl_school(id);
ALTER TABLE ONLY tbl_daybreak_study_application
    ADD CONSTRAINT fkau231getnne987q2dou1ehd8v FOREIGN KEY (student_id) REFERENCES tbl_student(id);
ALTER TABLE ONLY tbl_available_feature
    ADD CONSTRAINT fkcca5mtgektodebjrocbuk3vyb FOREIGN KEY (school_id) REFERENCES tbl_school(id);
ALTER TABLE ONLY tbl_remain_option
    ADD CONSTRAINT fkcke03u4we2b48be3gjeqekg36 FOREIGN KEY (school_id) REFERENCES tbl_school(id);
ALTER TABLE ONLY tbl_bug_attachment
    ADD CONSTRAINT fkcupnbmh76lx5x1fks4vlpvki2 FOREIGN KEY (bug_report_id) REFERENCES tbl_bug_report(id);
ALTER TABLE ONLY tbl_daybreak_study_application
    ADD CONSTRAINT fkfifhe7uq5ayqmf0cip4sevp33 FOREIGN KEY (type_id) REFERENCES tbl_daybreak_study_type(id);
ALTER TABLE ONLY tbl_vote
    ADD CONSTRAINT fkfywyll79shqjgblb9hgx7jq16 FOREIGN KEY (voting_topic_id) REFERENCES tbl_voting_topic(id);
ALTER TABLE ONLY tbl_vote
    ADD CONSTRAINT fkgoy6nfug45mmm8mut8o4v12vp FOREIGN KEY (student_id) REFERENCES tbl_student(id);
ALTER TABLE ONLY tbl_notice
    ADD CONSTRAINT fki2wj0xed0ekpmixcvd65mqs1t FOREIGN KEY (manager_id) REFERENCES tbl_manager(user_id);
ALTER TABLE ONLY tbl_vote
    ADD CONSTRAINT fkj6gni3626ec0o35qmquc9kuk5 FOREIGN KEY (selected_student_id) REFERENCES tbl_student(id);
ALTER TABLE ONLY tbl_room
    ADD CONSTRAINT fkjssn7mu4yy6d91vh705s0xekr FOREIGN KEY (school_id) REFERENCES tbl_school(id);
ALTER TABLE ONLY tbl_daybreak_study_application
    ADD CONSTRAINT fklmsgw3ptnm8u0e6i0cwx9jfkf FOREIGN KEY (school_id) REFERENCES tbl_school(id);
ALTER TABLE ONLY tbl_daybreak_study_type
    ADD CONSTRAINT fkloh72au8cqdrr92q7xped4vx5 FOREIGN KEY (school_id) REFERENCES tbl_school(id);
ALTER TABLE ONLY tbl_voting_option
    ADD CONSTRAINT fkmkox6c1xux6mbqrtx3n5ir46r FOREIGN KEY (voting_topic_id) REFERENCES tbl_voting_topic(id);
ALTER TABLE ONLY tbl_excluded_student
    ADD CONSTRAINT fkn5xowt10pg01hymhsxqy114pg FOREIGN KEY (student_id) REFERENCES tbl_student(id);
ALTER TABLE ONLY tbl_student
    ADD CONSTRAINT fkngjvmmejph90txmm2ri4owhj1 FOREIGN KEY (room_id) REFERENCES tbl_room(id);
ALTER TABLE ONLY tbl_remain_status
    ADD CONSTRAINT fkpajafk6llea5socoo9i1o6xsr FOREIGN KEY (student_id) REFERENCES tbl_student(id);
ALTER TABLE ONLY tbl_meal
    ADD CONSTRAINT fkrxg5eeld0m9bqlukmw5fahx77 FOREIGN KEY (school_id) REFERENCES tbl_school(id);
ALTER TABLE ONLY tbl_bug_report
    ADD CONSTRAINT fks30sj7yis4diyafu6w0xpn8md FOREIGN KEY (student_id) REFERENCES tbl_student(id);
ALTER TABLE ONLY tbl_point_history
    ADD CONSTRAINT fksqvrvqif93phnsftayqisvl2u FOREIGN KEY (school_id) REFERENCES tbl_school(id);
ALTER TABLE ONLY tbl_voting_topic
    ADD CONSTRAINT fksx5e7lc5705dasgu2myhb3bxy FOREIGN KEY (manager_id) REFERENCES tbl_manager(user_id);
ALTER TABLE ONLY tbl_vote
    ADD CONSTRAINT fktllvyffhwyeousodmuvlbb615 FOREIGN KEY (selected_option_id) REFERENCES tbl_voting_option(id);

-- ===== 운영에 있던 인덱스 (PostgreSQL은 FK 인덱스를 자동 생성하지 않습니다) =====
CREATE INDEX idx_bug_attachment_bug_report_id ON tbl_bug_attachment (bug_report_id);
CREATE INDEX idx_bug_report_student_id ON tbl_bug_report (student_id);
CREATE INDEX idx_daybreak_study_application_student_id ON tbl_daybreak_study_application (student_id);
CREATE INDEX idx_daybreak_study_application_teacher_id ON tbl_daybreak_study_application (teacher_id);
CREATE INDEX idx_daybreak_study_application_school_id ON tbl_daybreak_study_application (school_id);
CREATE INDEX idx_daybreak_study_application_type_id ON tbl_daybreak_study_application (type_id);
CREATE INDEX idx_daybreak_study_type_school_id ON tbl_daybreak_study_type (school_id);
CREATE INDEX idx_excluded_student_school_id ON tbl_excluded_student (school_id);
CREATE INDEX idx_meal_school_id ON tbl_meal (school_id);
CREATE INDEX idx_notice_manager_id ON tbl_notice (manager_id);
CREATE INDEX idx_point_history_school_id ON tbl_point_history (school_id);
CREATE INDEX idx_point_history_student_gcn_student_name_created_at ON tbl_point_history (student_gcn,student_name,created_at DESC);
CREATE INDEX idx_point_option_school_id ON tbl_point_option (school_id);
CREATE INDEX idx_remain_option_school_id ON tbl_remain_option (school_id);
CREATE INDEX idx_remain_status_remain_option_id ON tbl_remain_status (remain_option_id);
CREATE INDEX idx_student_user_id ON tbl_student (user_id);
CREATE INDEX idx_student_room_id ON tbl_student (room_id);
CREATE INDEX idx_student_tag_tag_id ON tbl_student_tag (tag_id);
CREATE INDEX idx_user_school_id ON tbl_user (school_id);
CREATE INDEX idx_vote_selected_option_id ON tbl_vote (selected_option_id);
CREATE INDEX idx_vote_selected_student_id ON tbl_vote (selected_student_id);
CREATE INDEX idx_vote_student_id ON tbl_vote (student_id);
CREATE INDEX idx_vote_voting_topic_id ON tbl_vote (voting_topic_id);
CREATE INDEX idx_voting_option_voting_topic_id ON tbl_voting_option (voting_topic_id);
CREATE INDEX idx_voting_topic_manager_id ON tbl_voting_topic (manager_id);

-- ===== 운영에 있던 UNIQUE (엔티티에 선언되지 않음) =====
ALTER TABLE tbl_daybreak_study_type ADD CONSTRAINT uk_daybreak_study_type_name UNIQUE (name);

-- ===== Quartz (quartz-2.3.2 tables_postgres.sql) =====
DROP TABLE IF EXISTS QRTZ_FIRED_TRIGGERS;
DROP TABLE IF EXISTS QRTZ_PAUSED_TRIGGER_GRPS;
DROP TABLE IF EXISTS QRTZ_SCHEDULER_STATE;
DROP TABLE IF EXISTS QRTZ_LOCKS;
DROP TABLE IF EXISTS QRTZ_SIMPLE_TRIGGERS;
DROP TABLE IF EXISTS QRTZ_CRON_TRIGGERS;
DROP TABLE IF EXISTS QRTZ_SIMPROP_TRIGGERS;
DROP TABLE IF EXISTS QRTZ_BLOB_TRIGGERS;
DROP TABLE IF EXISTS QRTZ_TRIGGERS;
DROP TABLE IF EXISTS QRTZ_JOB_DETAILS;
DROP TABLE IF EXISTS QRTZ_CALENDARS;

CREATE TABLE QRTZ_JOB_DETAILS
(
  SCHED_NAME        VARCHAR(120) NOT NULL,
  JOB_NAME          VARCHAR(200) NOT NULL,
  JOB_GROUP         VARCHAR(200) NOT NULL,
  DESCRIPTION       VARCHAR(250) NULL,
  JOB_CLASS_NAME    VARCHAR(250) NOT NULL,
  IS_DURABLE        BOOL         NOT NULL,
  IS_NONCONCURRENT  BOOL         NOT NULL,
  IS_UPDATE_DATA    BOOL         NOT NULL,
  REQUESTS_RECOVERY BOOL         NOT NULL,
  JOB_DATA          BYTEA        NULL,
  PRIMARY KEY (SCHED_NAME, JOB_NAME, JOB_GROUP)
);

CREATE TABLE QRTZ_TRIGGERS
(
  SCHED_NAME     VARCHAR(120) NOT NULL,
  TRIGGER_NAME   VARCHAR(200) NOT NULL,
  TRIGGER_GROUP  VARCHAR(200) NOT NULL,
  JOB_NAME       VARCHAR(200) NOT NULL,
  JOB_GROUP      VARCHAR(200) NOT NULL,
  DESCRIPTION    VARCHAR(250) NULL,
  NEXT_FIRE_TIME BIGINT       NULL,
  PREV_FIRE_TIME BIGINT       NULL,
  PRIORITY       INTEGER      NULL,
  TRIGGER_STATE  VARCHAR(16)  NOT NULL,
  TRIGGER_TYPE   VARCHAR(8)   NOT NULL,
  START_TIME     BIGINT       NOT NULL,
  END_TIME       BIGINT       NULL,
  CALENDAR_NAME  VARCHAR(200) NULL,
  MISFIRE_INSTR  SMALLINT     NULL,
  JOB_DATA       BYTEA        NULL,
  PRIMARY KEY (SCHED_NAME, TRIGGER_NAME, TRIGGER_GROUP),
  FOREIGN KEY (SCHED_NAME, JOB_NAME, JOB_GROUP)
  REFERENCES QRTZ_JOB_DETAILS (SCHED_NAME, JOB_NAME, JOB_GROUP)
);

CREATE TABLE QRTZ_SIMPLE_TRIGGERS
(
  SCHED_NAME      VARCHAR(120) NOT NULL,
  TRIGGER_NAME    VARCHAR(200) NOT NULL,
  TRIGGER_GROUP   VARCHAR(200) NOT NULL,
  REPEAT_COUNT    BIGINT       NOT NULL,
  REPEAT_INTERVAL BIGINT       NOT NULL,
  TIMES_TRIGGERED BIGINT       NOT NULL,
  PRIMARY KEY (SCHED_NAME, TRIGGER_NAME, TRIGGER_GROUP),
  FOREIGN KEY (SCHED_NAME, TRIGGER_NAME, TRIGGER_GROUP)
  REFERENCES QRTZ_TRIGGERS (SCHED_NAME, TRIGGER_NAME, TRIGGER_GROUP)
);

CREATE TABLE QRTZ_CRON_TRIGGERS
(
  SCHED_NAME      VARCHAR(120) NOT NULL,
  TRIGGER_NAME    VARCHAR(200) NOT NULL,
  TRIGGER_GROUP   VARCHAR(200) NOT NULL,
  CRON_EXPRESSION VARCHAR(120) NOT NULL,
  TIME_ZONE_ID    VARCHAR(80),
  PRIMARY KEY (SCHED_NAME, TRIGGER_NAME, TRIGGER_GROUP),
  FOREIGN KEY (SCHED_NAME, TRIGGER_NAME, TRIGGER_GROUP)
  REFERENCES QRTZ_TRIGGERS (SCHED_NAME, TRIGGER_NAME, TRIGGER_GROUP)
);

CREATE TABLE QRTZ_SIMPROP_TRIGGERS
(
  SCHED_NAME    VARCHAR(120)   NOT NULL,
  TRIGGER_NAME  VARCHAR(200)   NOT NULL,
  TRIGGER_GROUP VARCHAR(200)   NOT NULL,
  STR_PROP_1    VARCHAR(512)   NULL,
  STR_PROP_2    VARCHAR(512)   NULL,
  STR_PROP_3    VARCHAR(512)   NULL,
  INT_PROP_1    INT            NULL,
  INT_PROP_2    INT            NULL,
  LONG_PROP_1   BIGINT         NULL,
  LONG_PROP_2   BIGINT         NULL,
  DEC_PROP_1    NUMERIC(13, 4) NULL,
  DEC_PROP_2    NUMERIC(13, 4) NULL,
  BOOL_PROP_1   BOOL           NULL,
  BOOL_PROP_2   BOOL           NULL,
  PRIMARY KEY (SCHED_NAME, TRIGGER_NAME, TRIGGER_GROUP),
  FOREIGN KEY (SCHED_NAME, TRIGGER_NAME, TRIGGER_GROUP)
  REFERENCES QRTZ_TRIGGERS (SCHED_NAME, TRIGGER_NAME, TRIGGER_GROUP)
);

CREATE TABLE QRTZ_BLOB_TRIGGERS
(
  SCHED_NAME    VARCHAR(120) NOT NULL,
  TRIGGER_NAME  VARCHAR(200) NOT NULL,
  TRIGGER_GROUP VARCHAR(200) NOT NULL,
  BLOB_DATA     BYTEA        NULL,
  PRIMARY KEY (SCHED_NAME, TRIGGER_NAME, TRIGGER_GROUP),
  FOREIGN KEY (SCHED_NAME, TRIGGER_NAME, TRIGGER_GROUP)
  REFERENCES QRTZ_TRIGGERS (SCHED_NAME, TRIGGER_NAME, TRIGGER_GROUP)
);

CREATE TABLE QRTZ_CALENDARS
(
  SCHED_NAME    VARCHAR(120) NOT NULL,
  CALENDAR_NAME VARCHAR(200) NOT NULL,
  CALENDAR      BYTEA        NOT NULL,
  PRIMARY KEY (SCHED_NAME, CALENDAR_NAME)
);


CREATE TABLE QRTZ_PAUSED_TRIGGER_GRPS
(
  SCHED_NAME    VARCHAR(120) NOT NULL,
  TRIGGER_GROUP VARCHAR(200) NOT NULL,
  PRIMARY KEY (SCHED_NAME, TRIGGER_GROUP)
);

CREATE TABLE QRTZ_FIRED_TRIGGERS
(
  SCHED_NAME        VARCHAR(120) NOT NULL,
  ENTRY_ID          VARCHAR(95)  NOT NULL,
  TRIGGER_NAME      VARCHAR(200) NOT NULL,
  TRIGGER_GROUP     VARCHAR(200) NOT NULL,
  INSTANCE_NAME     VARCHAR(200) NOT NULL,
  FIRED_TIME        BIGINT       NOT NULL,
  SCHED_TIME        BIGINT       NOT NULL,
  PRIORITY          INTEGER      NOT NULL,
  STATE             VARCHAR(16)  NOT NULL,
  JOB_NAME          VARCHAR(200) NULL,
  JOB_GROUP         VARCHAR(200) NULL,
  IS_NONCONCURRENT  BOOL         NULL,
  REQUESTS_RECOVERY BOOL         NULL,
  PRIMARY KEY (SCHED_NAME, ENTRY_ID)
);

CREATE TABLE QRTZ_SCHEDULER_STATE
(
  SCHED_NAME        VARCHAR(120) NOT NULL,
  INSTANCE_NAME     VARCHAR(200) NOT NULL,
  LAST_CHECKIN_TIME BIGINT       NOT NULL,
  CHECKIN_INTERVAL  BIGINT       NOT NULL,
  PRIMARY KEY (SCHED_NAME, INSTANCE_NAME)
);

CREATE TABLE QRTZ_LOCKS
(
  SCHED_NAME VARCHAR(120) NOT NULL,
  LOCK_NAME  VARCHAR(40)  NOT NULL,
  PRIMARY KEY (SCHED_NAME, LOCK_NAME)
);

CREATE INDEX IDX_QRTZ_J_REQ_RECOVERY
  ON QRTZ_JOB_DETAILS (SCHED_NAME, REQUESTS_RECOVERY);
CREATE INDEX IDX_QRTZ_J_GRP
  ON QRTZ_JOB_DETAILS (SCHED_NAME, JOB_GROUP);

CREATE INDEX IDX_QRTZ_T_J
  ON QRTZ_TRIGGERS (SCHED_NAME, JOB_NAME, JOB_GROUP);
CREATE INDEX IDX_QRTZ_T_JG
  ON QRTZ_TRIGGERS (SCHED_NAME, JOB_GROUP);
CREATE INDEX IDX_QRTZ_T_C
  ON QRTZ_TRIGGERS (SCHED_NAME, CALENDAR_NAME);
CREATE INDEX IDX_QRTZ_T_G
  ON QRTZ_TRIGGERS (SCHED_NAME, TRIGGER_GROUP);
CREATE INDEX IDX_QRTZ_T_STATE
  ON QRTZ_TRIGGERS (SCHED_NAME, TRIGGER_STATE);
CREATE INDEX IDX_QRTZ_T_N_STATE
  ON QRTZ_TRIGGERS (SCHED_NAME, TRIGGER_NAME, TRIGGER_GROUP, TRIGGER_STATE);
CREATE INDEX IDX_QRTZ_T_N_G_STATE
  ON QRTZ_TRIGGERS (SCHED_NAME, TRIGGER_GROUP, TRIGGER_STATE);
CREATE INDEX IDX_QRTZ_T_NEXT_FIRE_TIME
  ON QRTZ_TRIGGERS (SCHED_NAME, NEXT_FIRE_TIME);
CREATE INDEX IDX_QRTZ_T_NFT_ST
  ON QRTZ_TRIGGERS (SCHED_NAME, TRIGGER_STATE, NEXT_FIRE_TIME);
CREATE INDEX IDX_QRTZ_T_NFT_MISFIRE
  ON QRTZ_TRIGGERS (SCHED_NAME, MISFIRE_INSTR, NEXT_FIRE_TIME);
CREATE INDEX IDX_QRTZ_T_NFT_ST_MISFIRE
  ON QRTZ_TRIGGERS (SCHED_NAME, MISFIRE_INSTR, NEXT_FIRE_TIME, TRIGGER_STATE);
CREATE INDEX IDX_QRTZ_T_NFT_ST_MISFIRE_GRP
  ON QRTZ_TRIGGERS (SCHED_NAME, MISFIRE_INSTR, NEXT_FIRE_TIME, TRIGGER_GROUP, TRIGGER_STATE);

CREATE INDEX IDX_QRTZ_FT_TRIG_INST_NAME
  ON QRTZ_FIRED_TRIGGERS (SCHED_NAME, INSTANCE_NAME);
CREATE INDEX IDX_QRTZ_FT_INST_JOB_REQ_RCVRY
  ON QRTZ_FIRED_TRIGGERS (SCHED_NAME, INSTANCE_NAME, REQUESTS_RECOVERY);
CREATE INDEX IDX_QRTZ_FT_J_G
  ON QRTZ_FIRED_TRIGGERS (SCHED_NAME, JOB_NAME, JOB_GROUP);
CREATE INDEX IDX_QRTZ_FT_JG
  ON QRTZ_FIRED_TRIGGERS (SCHED_NAME, JOB_GROUP);
CREATE INDEX IDX_QRTZ_FT_T_G
  ON QRTZ_FIRED_TRIGGERS (SCHED_NAME, TRIGGER_NAME, TRIGGER_GROUP);
CREATE INDEX IDX_QRTZ_FT_TG
  ON QRTZ_FIRED_TRIGGERS (SCHED_NAME, TRIGGER_GROUP);

-- 운영에 존재하던 락 행. Quartz가 없으면 만들지만 명시해 둡니다.
INSERT INTO QRTZ_LOCKS (SCHED_NAME, LOCK_NAME) VALUES ('DMS-Main-Scheduler', 'TRIGGER_ACCESS');
