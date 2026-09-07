# 데이터베이스

스키마 규약과 Flyway 운영 규칙을 정리한 문서입니다.

---

## 구성

| 항목 | 값 |
| --- | --- |
| RDBMS | MySQL 8.0 |
| 마이그레이션 | Flyway (V1 ~ V28) |
| ORM | JPA + QueryDSL |
| `ddl-auto` | **`validate`** — 앱은 스키마를 만들지 않습니다 |
| 캐시 | Redis (리프레시 토큰, 이메일 인증코드) |
| 스케줄러 저장소 | 같은 MySQL의 `QRTZ_*` |

> Redis에 든 값은 **영속화 설정이 없습니다.** 재시작하면 전원 로그아웃됩니다.

---

## ERD

ERD 이미지는 [README](../README.md#erd)에 있습니다.

> ⚠️ 이미지의 최신 여부는 보장되지 않습니다. **현재 스키마의 정답은 마이그레이션 SQL과 엔티티 클래스**입니다.

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

* **PK**: 시간 기반 UUID를 `BINARY(16)`으로 저장. `BaseEntity`(id + createdAt) / `BaseUUIDEntity`(id) / `BaseTimeEntity`(createdAt)를 상속
* **Soft delete**: `deleted_at` + `@SQLRestriction("deleted_at is null")`
* **테이블명**: `tbl_` 접두사 + snake_case
* **enum 컬럼**: `@Enumerated(EnumType.STRING)` + `columnDefinition = "VARCHAR(n)"`
* **FK**: `BINARY(16)`, `@ManyToOne(fetch = LAZY)` 기본

---

## Flyway

### 위치와 이름

```text
main-infrastructure/src/main/resources/db/migration/V<버전>__<snake_case_설명>.sql
```

파일 맨 위에 **왜 이 변경이 필요한지 한 줄 주석**을 답니다.

### 규칙

* **이미 적용된 마이그레이션은 절대 수정하지 않습니다.** checksum이 깨져 다음 배포가 실패합니다
* **새 변경은 항상 최신 번호 뒤에 붙입니다.** 운영 DB에 이미 적용된 최고 버전보다 낮은 번호를 쓰면 안 됩니다
* 번호를 이미 뺏겼다면 **서브버전**을 씁니다 — `V21_1`은 버전 21.1로 해석되어 V21과 V22 사이에 들어갑니다 (기존 파일명을 안 건드림)
* 뒤늦게 넣는 `CREATE TABLE`은 **그 테이블이 처음 만들어졌던 시점의 스키마**로 작성합니다. 이후 `ALTER`들이 재적용되며 최종 상태에 도달하는 게 올바른 순서입니다
* **엔티티를 바꾸면 마이그레이션을 반드시 함께 추가**합니다 (`ddl-auto: validate`)

> ⚠️ **서브버전은 "빈 DB에서 스키마를 처음부터 재현하기 위한" 보완이지, 운영 DB에 뒤늦게 반영하는 수단이 아닙니다.**
>
> `spring.flyway.out-of-order`를 설정하지 않았으므로 **기본값 `false`** 입니다. 이미 더 높은 버전이 적용된 DB에서는
> 나중에 추가된 낮은 버전이 **실행되지 않습니다.**
>
> 실제 사례가 `V21_1__create_notification_tables.sql`입니다. notification이 별도 서비스였을 때 이 테이블들은 그쪽의
> 베이스라인이라 `CREATE TABLE`이 이 리포에 없었고, `ALTER`(V22~V25)만 넘어와서 빈 DB의 Flyway가 실패했습니다.
> 이 파일은 **그 구멍을 메우는 용도**이고, 해당 테이블이 이미 있는 운영 DB에서는 돌면 안 됩니다(중복 생성 에러).
> 이미 데이터가 있는 물리 DB를 합치는 건 마이그레이션 파일로 풀 문제가 아니라 별도 운영 작업입니다.

### `validate`가 잡지 못하는 것

**컬럼 길이와 인덱스는 검사하지 않습니다.** 어긋나도 부팅은 성공하니 직접 챙겨야 합니다.

* enum 값을 추가할 땐 **이름 길이가 `VARCHAR(n)`에 들어가는지** 세어보세요
* **인덱스는 반드시 마이그레이션으로** 넣으세요. 서버에서 직접 만들면 새 환경에서 조용히 누락됩니다

### 베이스라인이 없습니다

`V1`이 이미 존재하는 테이블을 `ALTER`하는 것으로 시작합니다. 초기 `CREATE TABLE`이 리포에 없어
**빈 DB에 Flyway만 돌려서는 스키마가 만들어지지 않습니다.** 새 환경은 덤프 복원이 전제입니다.

기존 DB를 옮길 때는 `flyway_schema_history`를 **직접 조회해** 마지막 성공 버전을 확인하고, 그 값으로 baseline을 잡습니다(추측 금지).

`application.yml`의 값은 이렇습니다.

```yaml
  flyway:
    baseline-on-migrate: ${BASELINE_ON_MIGRATE}
    baseline-version: 0          # ← 리터럴. 환경변수로 덮어쓰는 것이 전제
```

**`baseline-version`을 그대로 두면 baseline이 0으로 잡혀 `V1`부터 재생됩니다.** `V1`은 이미 존재하는
`tbl_outing_application`을 `ALTER`하므로 옮긴 DB에서 실패합니다. 그래서 이관 때는 반드시 이렇게 합니다.

1. 원본 DB의 `flyway_schema_history`를 조회해 **마지막 성공 버전**을 확인한다
2. `BASELINE_ON_MIGRATE=true` + `SPRING_FLYWAY_BASELINE_VERSION=<확인한 버전>` 으로 **최초 1회만** 기동한다
3. baseline 이후 버전만 재생되므로, 그 구간의 `DROP TABLE` 대상 테이블은 **스키마만**(`mysqldump --no-data`) 미리 만들어둔다

> ⚠️ **`.env`에 값을 넣는 것만으로는 반영되지 않습니다.** docker-compose에 그 환경변수를 컨테이너로 전달하는 줄이
> 없으면 조용히 `0`으로 떨어집니다 — 실제로 이걸 빠뜨려 baseline이 0이 된 실패가 있었습니다.

---

## 마이그레이션 체크리스트

* [ ] 엔티티 변경에 대응하는 마이그레이션이 있는가
* [ ] 파일명 규칙을 지켰는가 (번호가 이미 있으면 서브버전)
* [ ] 이유 주석을 달았는가
* [ ] enum 이름 길이가 컬럼에 들어가는가
* [ ] 인덱스를 마이그레이션에 넣었는가
* [ ] 로컬에서 앱을 띄워 Flyway 통과를 확인했는가

---

## 알려진 스키마 부채

* **`tbl_point_history`에 학생 FK가 없습니다.** 이름 + 학번 문자열(GCN) 스냅샷으로만 학생과 잇습니다.
  진급하면 GCN이 바뀌어 과거 이력과 끊기고, 문자열 재조립 때문에 쿼리가 복잡해집니다. `minus_total`은 상쇄 항목이 있어 **단조 증가가 아닙니다.**
* **일부 인덱스와 인프라 설정이 형상관리 밖(운영 서버)에만 있습니다.**
* 운영 DB 이름은 `dms`가 아니라 `prod_dms`입니다.

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

운영 DB는 매일 03:00 KST에 systemd timer로 덤프 → gzip → 검증 → S3 업로드됩니다.

원칙 3가지:

* 로컬 디스크만으론 부족합니다 (인스턴스 소실이 복구 대상이면 덤프도 함께 사라짐)
* **검증 없는 백업이 최악입니다** — `mysqldump | gzip`은 덤프가 죽어도 종료코드 0입니다
* 서버에 삭제 권한을 주지 않습니다. 보존은 S3 라이프사이클이 담당합니다

---

## 자주 겪는 문제

| 증상 | 원인 |
| --- | --- |
| V1부터 Flyway 실패 | 빈 DB. 베이스라인 없음 → 덤프 복원 |
| `Schema-validation: missing column` | 엔티티만 고치고 마이그레이션 누락 |
| `checksum mismatch` | 적용된 마이그레이션 파일을 수정함 |
| 새 환경만 유독 느림 | 인덱스가 마이그레이션에 없음 |
| 스케줄러 중복 실행 | 로컬 앱을 운영 DB에 붙임 (Quartz JobStore 공유) |
