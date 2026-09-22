-- 테스트 컨테이너는 ddl-auto: create-drop 로 스키마를 만들기 때문에 Flyway(V2)를 타지 않는다.
-- vector 확장이 없으면 vector(768) 컬럼 생성이 실패하므로 컨테이너 기동 직후에 만든다.
CREATE EXTENSION IF NOT EXISTS vector;
