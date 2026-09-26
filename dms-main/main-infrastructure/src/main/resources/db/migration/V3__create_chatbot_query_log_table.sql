-- #1105 챗봇 질의 로그: STUFFING(문서 전문)과 RAG(검색 청크)의 토큰 사용량·응답 시간 비교용
CREATE TABLE tbl_chatbot_query_log
(
    id                  uuid         NOT NULL PRIMARY KEY,
    school_id           uuid         NOT NULL,
    question            text         NOT NULL,
    answer_mode         varchar(20)  NOT NULL,
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
