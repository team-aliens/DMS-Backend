# 데이터베이스

스키마 규약과 Flyway 운영 규칙을 정리한 문서입니다.

---

## 구성

| 항목 | 값 |
| --- | --- |
| RDBMS | PostgreSQL 16 (`pgvector/pgvector:pg16`) |
| 마이그레이션 | Flyway — `V1__baseline.sql` 하나가 전체 스키마 |
| ORM | JPA + QueryDSL |
| `ddl-auto` | **`validate`** — 앱은 스키마를 만들지 않습니다 |
| 캐시 | Redis (리프레시 토큰, 이메일 인증코드) |
| 스케줄러 저장소 | 같은 PostgreSQL의 `QRTZ_*` (`PostgreSQLDelegate`) |

> Redis에 든 값은 **영속화 설정이 없습니다.** 재시작하면 전원 로그아웃됩니다.

> ⚠️ **코드는 PostgreSQL, 운영 DB는 아직 MySQL 8.0입니다.** 이 문서의 스키마·Flyway 내용은 PostgreSQL 기준이고,
> [백업](#백업) 절만 이관 전 현황(MySQL)입니다. 데이터 이관과 컷오버는 별도 작업입니다.
>
> 전환 범위: 엔티티 `columnDefinition` 124곳 제거, Flyway V1~V28(29파일) → `V1__baseline.sql`,
> 드라이버·방언·Quartz delegate 교체, 테스트 컨테이너를 `pgvector/pgvector:pg16`으로.
> 이미지에 pgvector가 들어 있는 건 챗봇 RAG의 벡터 컬럼을 대비한 선택입니다.

---

## ERD
![DMS-ERD.png](images/DMS-ERD.png)
### 테이블 (도메인별)

| 도메인 | 테이블 |
| --- | --- |
| 사용자 | `tbl_user`, `tbl_student`, `tbl_manager`, `tbl_teacher` |
| 학교 | `tbl_school`, `tbl_available_feature`, `tbl_room` |
| 상벌점 | `tbl_point_history`, `tbl_point_option`, `tbl_point_filter`, `tbl_phrase` |
| 태그 | `tbl_tag`, `tbl_student_tag` |
| 새벽자습 | `tbl_daybreak_study_type`, `tbl_daybreak_study_application` |
| 알림 | `tbl_device_token`, `tbl_notification_of_user`, `tbl_topic_subscription` |
| 공지·급식 | `tbl_notice`, `tbl_meal` |
| 투표 | `tbl_voting_topic`, `tbl_voting_option`, `tbl_vote`, `tbl_excluded_student` |
| 잔류 | `tbl_remain_option`, `tbl_remain_status`, `tbl_remain_available_time` |
| 버그 제보 | `tbl_bug_report` (+ 첨부) |
| 스케줄러 | `QRTZ_*` |

---

## 스키마 규약

* **PK**: 시간 기반 UUID를 PostgreSQL `uuid` 타입에 저장. `BaseEntity`(id + createdAt) / `BaseUUIDEntity`(id) / `BaseTimeEntity`(createdAt)를 상속
* **Soft delete**: `deleted_at` + `@SQLRestriction("deleted_at is null")`
* **테이블명**: `tbl_` 접두사 + snake_case
* **enum 컬럼**: `@Enumerated(EnumType.STRING)` + `length = n` (n은 **가장 긴 상수 이름보다 커야 합니다** — 아래 `validate` 절)
* **FK**: `uuid`, `@ManyToOne(fetch = LAZY)` 기본
* **`columnDefinition`에 DB 타입 문자열을 쓰지 않습니다.** 자세한 규칙은 [code-convention.md](./code-convention.md) "JPA 엔티티 매핑" 절
* **인덱스 이름은 `idx_<테이블>_<컬럼>`으로 짓습니다.** PostgreSQL의 인덱스 이름은 **테이블이 아니라 스키마 전역에서 유일**해야 합니다 — `school_id` 같은 이름을 여러 테이블에 쓰면 두 번째부터 `relation "school_id" already exists`로 실패합니다
* **FK 컬럼에는 인덱스를 직접 만듭니다.** MySQL은 FK를 걸면 인덱스를 자동 생성했지만 **PostgreSQL은 만들지 않습니다.** baseline에 있는 인덱스 대부분이 이것입니다
* **이메일은 대소문자를 구분합니다.** MySQL의 ci collation이 비교에서 무시해주던 동작을 PostgreSQL에서 재현하지 않기로 했습니다(`ihansaem`과 `Ihansaem`은 다른 주소). 저장값은 입력 원본 그대로 두고, `lower()` 비교나 `citext`를 쓰지 않습니다

---

## Flyway

### 위치와 이름

```text
main-infrastructure/src/main/resources/db/migration/V<버전>__<snake_case_설명>.sql
```

파일 맨 위에 **왜 이 변경이 필요한지 한 줄 주석**을 답니다.

### 규칙

* **이미 적용된 마이그레이션은 절대 수정하지 않습니다.** checksum이 깨져 다음 배포가 실패합니다
* **새 변경은 항상 최신 번호 뒤에 붙입니다.** 지금 최신은 `V1__baseline.sql`이므로 다음 변경은 `V2`입니다
* 번호를 이미 뺏겼다면 **서브버전**을 씁니다 — `V2_1`은 버전 2.1로 해석되어 V2와 V3 사이에 들어갑니다 (기존 파일명을 안 건드림)
* **엔티티를 바꾸면 마이그레이션을 반드시 함께 추가**합니다 (`ddl-auto: validate`)

> ⚠️ **서브버전은 "빈 DB에서 스키마를 처음부터 재현하기 위한" 보완이지, 운영 DB에 뒤늦게 반영하는 수단이 아닙니다.**
>
> `spring.flyway.out-of-order`를 설정하지 않았으므로 **기본값 `false`** 입니다. 이미 더 높은 버전이 적용된 DB에서는
> 나중에 추가된 낮은 버전이 **실행되지 않습니다.** 빈 DB에서는 만들어지고 운영에서는 안 만들어지는 테이블이 생깁니다.

### `validate`가 잡지 못하는 것

**컬럼 길이와 인덱스는 검사하지 않습니다.** 어긋나도 부팅은 성공하니 직접 챙겨야 합니다.

* enum 값을 추가할 땐 **이름 길이가 `length = n`에 들어가는지** 세어보세요. 부팅으로는 안 걸리고 INSERT에서 터집니다
* **인덱스는 반드시 마이그레이션으로** 넣으세요. 서버에서 직접 만들면 새 환경에서 조용히 누락됩니다

### 베이스라인 — `V1`이 전체 스키마입니다

PostgreSQL로 옮기면서 Flyway 히스토리를 새로 시작했고, **`V1__baseline.sql` 하나가 앱 테이블 29개 + Quartz 11개를 전부 만듭니다.**
빈 DB에 Flyway만 돌려도 스키마가 완성되므로 **새 환경에 덤프 복원이 필요 없습니다.**

`V1`은 손으로 번역하지 않았습니다. 두 방향을 모두 뽑아 대조해서 만들었습니다.

```
① 엔티티 → Hibernate ddl-auto:create → 빈 PG → pg_dump   (validate 통과가 구조적으로 보장됨)
② 운영 MySQL 덤프(mysqldump --no-data)                    (인덱스·제약·nullability의 진실)
③ ①을 뼈대로 ②와 대조해 차이를 보정                       ← V1__baseline.sql
```

①만 쓰면 **DB에만 있고 엔티티에 선언이 없는 것들**을 잃고(실제로 인덱스 25개와 UNIQUE 1개가 그랬습니다),
②만 쓰면 Hibernate가 기대하는 타입과 어긋나 `validate`가 기동을 막습니다.

베이스라인을 손보게 되면 같은 방법으로 다시 대조하세요. 검증 종료 조건은 **빈 PG 컨테이너 → Flyway migrate → `validate` → 스프링 컨텍스트 기동**까지입니다.
컴파일과 단위 테스트는 DB를 띄우지 않으므로 스키마에 대해 아무것도 증명하지 않습니다.

> ⚠️ **팀원 로컬 DB는 새로 만드세요.** 기존 MySQL 로컬에는 옛 히스토리(V1~V28)가 남아 있어 새 `V1`과 checksum이 어긋납니다.
> `flyway repair`가 아니라 **PostgreSQL 컨테이너를 새로 띄우는 것**이 정답입니다.

`application.yml`의 Flyway 설정은 이렇습니다.

```yaml
  flyway:
    enabled: true
    baseline-on-migrate: ${BASELINE_ON_MIGRATE}
    baseline-version: 0
```

`baseline-on-migrate`는 **히스토리 테이블이 없는 비어 있지 않은 DB**에만 관여합니다. 새로 만든 빈 PostgreSQL에는
해당이 없으니 `BASELINE_ON_MIGRATE=false`로 둡니다. 이 값은 **플레이스홀더라 환경변수가 없으면 기동이 실패**하므로
로컬에서도 `.env`나 compose에 반드시 넣어야 합니다.

---

## 마이그레이션 체크리스트

* [ ] 엔티티 변경에 대응하는 마이그레이션이 있는가
* [ ] 파일명 규칙을 지켰는가 (번호가 이미 있으면 서브버전)
* [ ] 이유 주석을 달았는가
* [ ] enum 이름 길이가 컬럼에 들어가는가
* [ ] 인덱스를 마이그레이션에 넣었는가
* [ ] **인덱스 이름이 스키마 전역에서 유일한가** (`idx_<테이블>_<컬럼>`)
* [ ] **새 FK 컬럼에 인덱스를 직접 만들었는가** (PostgreSQL은 자동 생성하지 않습니다)
* [ ] **`columnDefinition`에 DB 타입 문자열을 쓰지 않았는가**
* [ ] 로컬에서 **빈 PostgreSQL에** 앱을 띄워 Flyway + `validate` 통과를 확인했는가

---

## 알려진 스키마 부채

* **`tbl_point_history`에 학생 FK가 없습니다.** 이름 + 학번 문자열(GCN) 스냅샷으로만 학생과 잇습니다.
  진급하면 GCN이 바뀌어 과거 이력과 끊기고, 문자열 재조립 때문에 쿼리가 복잡해집니다. `minus_total`은 상쇄 항목이 있어 **단조 증가가 아닙니다.**
* **인프라 설정(compose, 백업 타이머, 모니터링)은 여전히 형상관리 밖(운영 서버)에만 있습니다.**
  인덱스는 baseline을 만들 때 운영 덤프와 대조해 흡수했으므로 **인덱스 부채는 해소됐습니다.**
* **`tbl_point_option.created_at`이 baseline에서 nullable입니다.** 엔티티(`BaseTimeEntity`)는 `NOT NULL`인데
  운영에 NULL 16건이 있어 이관이 실패하지 않도록 열어뒀습니다. 그 16건을 백필하면 `NOT NULL`로 되돌릴 수 있습니다.
* **환경변수 이름이 아직 `MAIN_MYSQL_HOST`입니다.** 값은 PostgreSQL 호스트를 가리킵니다 — 컷오버 때 이름을 바꿉니다.
* 운영 DB 이름은 `dms`가 아니라 `prod_dms`입니다 (dev는 `stag_dms`).

---

## QueryDSL 주의

JPA/HQL 계층을 거치므로 SQL을 그대로 쓰면 안 되는 지점이 있습니다.

* `cast` 대상은 SQL 타입명이 아니라 **HQL 타입명**을 씁니다
* **`IN` 서브쿼리의 `.limit()`은 조용히 사라집니다** — 에러 없이 결과만 틀립니다
* 인덱스 컬럼을 **가공하면 인덱스를 못 탑니다.** 가공은 반대편으로 옮기세요
* 상관 서브쿼리는 인덱스로 해결되지 않습니다 — 실행 **횟수**를 줄여야 합니다

> **위 함정들은 mockk로 포트를 스텁한 테스트를 전부 통과합니다.**
> 템플릿 표현식이나 서브쿼리가 든 어댑터 메서드는 실제 DB로 돌려보세요 → [testing.md](./testing.md)

---

## 백업

> **이 절은 이관 전 현황(MySQL)입니다.** 컷오버 때 `pg_dump`로 재작성하고 복원 리허설까지 해야 합니다.

운영 DB는 매일 03:00 KST에 systemd timer로 덤프 → gzip → 검증 → S3 업로드됩니다.

원칙 3가지:

* 로컬 디스크만으론 부족합니다 (인스턴스 소실이 복구 대상이면 덤프도 함께 사라짐)
* **검증 없는 백업이 최악입니다** — `mysqldump | gzip`은 덤프가 죽어도 종료코드 0입니다. `pg_dump`로 바꿀 때도 같은 함정이 있으니 파이프라인 종료코드(`PIPESTATUS`)를 직접 봐야 합니다
* 서버에 삭제 권한을 주지 않습니다. 보존은 S3 라이프사이클이 담당합니다

