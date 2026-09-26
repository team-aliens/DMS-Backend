-- 이슈 #1105 챗봇 질의 로그
-- 질문 하나당 한 행. STUFFING(문서 전문)과 RAG(검색 청크) 방식의 토큰 사용량·응답 시간을 비교하려고 남긴다
CREATE TABLE tbl_chatbot_query_log (
    id uuid NOT NULL,
    school_id uuid NOT NULL,
    question text NOT NULL,
    -- STUFFING, RAG
    answer_mode character varying(20) NOT NULL,
    -- ANSWERED, NO_CONTEXT, FAILED
    status character varying(20) NOT NULL,
    -- 실패하면 null
    answer text,
    -- 쉼표로 이은 청크 id(없으면 빈 문자열). 청크는 문서를 다시 올리면 교체되므로 FK 를 걸지 않는다
    retrieved_chunk_ids text NOT NULL,
    -- Gemini usageMetadata. 값이 없으면 0
    prompt_tokens integer NOT NULL,
    cached_tokens integer NOT NULL,
    thoughts_tokens integer NOT NULL,
    candidates_tokens integer NOT NULL,
    total_tokens integer NOT NULL,
    response_time_ms bigint NOT NULL,
    created_at timestamp(6) without time zone NOT NULL
);

ALTER TABLE ONLY tbl_chatbot_query_log
    ADD CONSTRAINT tbl_chatbot_query_log_pkey PRIMARY KEY (id);

ALTER TABLE ONLY tbl_chatbot_query_log
    ADD CONSTRAINT fk_chatbot_query_log_school_id FOREIGN KEY (school_id) REFERENCES tbl_school(id);

-- PostgreSQL 은 FK 컬럼에 인덱스를 자동으로 만들지 않는다
CREATE INDEX idx_chatbot_query_log_school_id ON tbl_chatbot_query_log (school_id);
-- 월별 집계용
CREATE INDEX idx_chatbot_query_log_created_at ON tbl_chatbot_query_log (created_at);
