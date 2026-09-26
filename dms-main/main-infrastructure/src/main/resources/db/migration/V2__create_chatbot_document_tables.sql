-- #1104 챗봇 문서 인제스천: 업로드한 md/txt 파일 = document, 헤딩 단위로 자른 결과 = chunk
CREATE EXTENSION IF NOT EXISTS vector;

CREATE TABLE tbl_chatbot_document
(
    id              uuid                           NOT NULL PRIMARY KEY,
    school_id       uuid                           NOT NULL,
    title           varchar(100)                   NOT NULL,
    -- 업로드한 파일 이름. (school_id, source_uri) 가 문서의 키라서 같은 이름으로 다시 올리면 교체된다
    source_uri      varchar(500)                   NOT NULL,
    -- 원문 SHA-256. 해시와 임베딩 모델이 둘 다 같으면 재인제스천(재임베딩)을 건너뛴다
    content_hash    varchar(64)                    NOT NULL,
    -- 이 문서의 청크를 임베딩한 모델("모델명/차원"). 설정과 다르면 재임베딩 대상이다
    embedding_model varchar(50)                    NOT NULL,
    created_at      timestamp(6) WITHOUT TIME ZONE NOT NULL,
    updated_at      timestamp(6) WITHOUT TIME ZONE NOT NULL,
    published_at    timestamp(6) WITHOUT TIME ZONE NOT NULL,

    CONSTRAINT uk_chatbot_document_school_id_source_uri UNIQUE (school_id, source_uri),
    CONSTRAINT fk_chatbot_document_school_id FOREIGN KEY (school_id) REFERENCES tbl_school (id)
);

CREATE INDEX idx_chatbot_document_school_id
    ON tbl_chatbot_document (school_id);

CREATE TABLE tbl_chatbot_document_chunk
(
    id           uuid                           NOT NULL PRIMARY KEY,
    document_id  uuid                           NOT NULL,
    -- 문서와 중복이지만, 검색 필터(학교 격리)를 조인 없이 걸기 위해 둔다
    school_id    uuid                           NOT NULL,
    -- 문서 안에서의 순서. 검색된 청크의 이웃(같은 섹션 형제)을 확장할 때 쓴다
    order_index  integer                        NOT NULL,
    -- 예: "Ⅷ. 벌점 및 다벌점 교육 > 1. 벌점 항목 > 공동생활 방해"
    section_path varchar(300)                   NOT NULL,
    content      text                           NOT NULL,
    -- 저장 전에 L2 정규화하므로 코사인 거리 = 내적. 차원을 바꾸면 이 컬럼과 엔티티를 함께 고쳐야 한다
    embedding    vector(768)                    NOT NULL,
    created_at   timestamp(6) WITHOUT TIME ZONE NOT NULL,

    CONSTRAINT uk_chatbot_document_chunk_document_id_order_index UNIQUE (document_id, order_index),
    -- 청크는 문서에서 계산되는 파생물이라 문서가 삭제되면 같이 삭제된다
    CONSTRAINT fk_chatbot_document_chunk_document_id FOREIGN KEY (document_id) REFERENCES tbl_chatbot_document (id) ON DELETE CASCADE,
    CONSTRAINT fk_chatbot_document_chunk_school_id FOREIGN KEY (school_id) REFERENCES tbl_school (id)
);

CREATE INDEX idx_chatbot_document_chunk_school_id
    ON tbl_chatbot_document_chunk (school_id);
