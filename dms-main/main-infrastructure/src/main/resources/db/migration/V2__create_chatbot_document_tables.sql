-- 이슈 #1104 챗봇 문서 인제스천
-- 매니저가 올린 md/txt 파일 하나 = tbl_chatbot_document 한 행, 그 파일을 헤딩 단위로 자른 결과 = tbl_chatbot_document_chunk.
-- 유사도 검색은 pgvector 가 한다(청크의 embedding 컬럼). 확장 생성에는 슈퍼유저 권한이 필요하다.
CREATE EXTENSION IF NOT EXISTS vector;

CREATE TABLE tbl_chatbot_document (
    id uuid NOT NULL,
    school_id uuid NOT NULL,
    title character varying(100) NOT NULL,
    -- 업로드한 파일 이름. (school_id, source_uri) 가 문서의 키라서 같은 이름으로 다시 올리면 교체된다
    source_uri character varying(500) NOT NULL,
    -- 원문 SHA-256. 해시와 임베딩 모델이 둘 다 같으면 재인제스천(재임베딩)을 건너뛴다
    content_hash character varying(64) NOT NULL,
    -- 이 문서의 청크를 임베딩한 모델("모델명/차원"). 설정과 다르면 재임베딩 대상이다
    embedding_model character varying(50) NOT NULL,
    created_at timestamp(6) without time zone NOT NULL,
    updated_at timestamp(6) without time zone NOT NULL,
    published_at timestamp(6) without time zone NOT NULL
);

CREATE TABLE tbl_chatbot_document_chunk (
    id uuid NOT NULL,
    document_id uuid NOT NULL,
    -- 문서와 중복이지만, 검색 필터(학교 격리)를 조인 없이 걸기 위해 둔다
    school_id uuid NOT NULL,
    -- 문서 안에서의 순서. 검색된 청크의 이웃(같은 섹션 형제)을 확장할 때 쓴다
    order_index integer NOT NULL,
    -- 예: "Ⅷ. 벌점 및 다벌점 교육 > 1. 벌점 항목 > 공동생활 방해"
    section_path character varying(300) NOT NULL,
    content text NOT NULL,
    -- 저장 전에 L2 정규화하므로 코사인 거리 = 내적. 차원을 바꾸면 이 컬럼과 엔티티를 함께 고쳐야 한다
    embedding vector(768) NOT NULL,
    created_at timestamp(6) without time zone NOT NULL
);

ALTER TABLE ONLY tbl_chatbot_document
    ADD CONSTRAINT tbl_chatbot_document_pkey PRIMARY KEY (id);

ALTER TABLE ONLY tbl_chatbot_document_chunk
    ADD CONSTRAINT tbl_chatbot_document_chunk_pkey PRIMARY KEY (id);

ALTER TABLE ONLY tbl_chatbot_document
    ADD CONSTRAINT uk_chatbot_document_school_id_source_uri UNIQUE (school_id, source_uri);

ALTER TABLE ONLY tbl_chatbot_document_chunk
    ADD CONSTRAINT uk_chatbot_document_chunk_document_id_order_index UNIQUE (document_id, order_index);

ALTER TABLE ONLY tbl_chatbot_document
    ADD CONSTRAINT fk_chatbot_document_school_id FOREIGN KEY (school_id) REFERENCES tbl_school(id);

-- 청크는 문서에서 계산되는 파생물이라 문서가 삭제되면 같이 삭제된다
ALTER TABLE ONLY tbl_chatbot_document_chunk
    ADD CONSTRAINT fk_chatbot_document_chunk_document_id FOREIGN KEY (document_id) REFERENCES tbl_chatbot_document(id) ON DELETE CASCADE;

ALTER TABLE ONLY tbl_chatbot_document_chunk
    ADD CONSTRAINT fk_chatbot_document_chunk_school_id FOREIGN KEY (school_id) REFERENCES tbl_school(id);

-- PostgreSQL 은 FK 컬럼에 인덱스를 자동으로 만들지 않는다
CREATE INDEX idx_chatbot_document_school_id ON tbl_chatbot_document (school_id);
CREATE INDEX idx_chatbot_document_chunk_school_id ON tbl_chatbot_document_chunk (school_id);

-- embedding 에는 ANN(HNSW) 인덱스를 걸지 않는다. 청크가 수백 개 수준이라 전수 스캔이 더 빠르고,
-- 인덱스는 검색 지연이 실측으로 문제가 됐을 때 추가한다(수만~수십만 벡터부터).
