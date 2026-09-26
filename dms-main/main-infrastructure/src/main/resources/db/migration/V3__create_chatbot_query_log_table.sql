-- #1105 챗봇 질의 로그: 질문별 검색 청크·토큰 사용량·응답 시간 기록
CREATE TABLE tbl_chatbot_query_log
(
    id                  uuid         NOT NULL PRIMARY KEY,
    school_id           uuid         NOT NULL,
    question            text         NOT NULL,
    status              varchar(20)  NOT NULL,
    answer              text,
    -- 쉼표로 이은 청크 id. 청크는 문서를 다시 올리면 교체되므로 FK 를 걸지 않는다
    retrieved_chunk_ids text         NOT NULL,
    prompt_tokens       integer      NOT NULL,
    cached_tokens       integer      NOT NULL,
    thoughts_tokens     integer      NOT NULL,
    candidates_tokens   integer      NOT NULL,
    total_tokens        integer      NOT NULL,
    response_time_ms    bigint       NOT NULL,
    created_at          timestamp(6) WITHOUT TIME ZONE NOT NULL,

    CONSTRAINT fk_chatbot_query_log_school_id FOREIGN KEY (school_id) REFERENCES tbl_school (id)
);

CREATE INDEX idx_chatbot_query_log_school_id
    ON tbl_chatbot_query_log (school_id);

CREATE INDEX idx_chatbot_query_log_created_at
    ON tbl_chatbot_query_log (created_at);
