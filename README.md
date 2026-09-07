# 시스템 소개

DMS는 효율적인 기숙사 관리를 위한 **기숙사 관리 시스템**입니다.
현재 학생 약 180명, 선생님 약 30명이 실사용하고 있습니다.

- **학생**은 사감선생님께 직접 찾아갈 필요 없이 앱으로 신청하고 필요한 정보를 확인할 수 있습니다.
- **선생님**은 웹으로 학생 정보·신청 현황·상벌점 내역을 모아서 조회하고 관리할 수 있습니다.

주요 기능: 인증/회원 · 학생 관리 · 상벌점 · 경고 태그 · 새벽자습 신청/승인 · 공지 · 급식 · 알림(FCM) · 투표 · 잔류 · 챗봇

---

# 기술 스택

Kotlin · Spring Boot 3.2 · Java 17 · MySQL 8 · Redis · JPA/QueryDSL · Flyway · Quartz · Docker · AWS EC2/ECR · GitHub Actions

---

# 문서 목록

- [architecture.md](docs/architecture.md) — 시스템 아키텍처
- [database.md](docs/database.md) — DB 설계 및 flyway 사용법
- [git-convention.md](docs/git-convention.md) — Git 관련 규칙
- [testing.md](docs/testing.md) — 테스트 코드 작성 방법