# Git 컨벤션

이슈 → 브랜치 → 커밋 → PR 순서로 진행합니다.

---

## 📌 Issue

**작업 전에 이슈를 먼저 만듭니다.** 브랜치·커밋·PR이 전부 이슈 번호를 참조합니다.

* 템플릿 2종: `do.yml`(Todo / `feat` 라벨), `fix.yml`(Bug / `👾bug` 라벨)
* 둘 다 `### Describe`(체크박스 목록) + `### Additional`(없으면 `_No response_`) 구조입니다

---

## 🌳 Branch Rule

```text
<type>/<이슈번호>-<작업내용>
```

* `type`은 커밋 타입과 같은 값을 씁니다
* 작업내용은 영문 kebab-case로 짧게

```text
feat/1059-revert-application
feat/1066-daybreak-study-application-cancel
fix/1074-general-teacher-sort
docs/1080-onboarding
```

### 브랜치 구조

```text
main            운영 배포 브랜치
 └── develop    기본 개발 브랜치
       ├── feat/1059-revert-application
       └── fix/1074-general-teacher-sort
```

* 작업 브랜치는 **`develop`에서 따고 `develop`으로 PR**합니다
* `main`은 develop을 머지해 배포할 때만 갱신합니다

> ⚠️ **`main` push는 곧 운영 배포입니다.** `prod-cd.yml`이 트리거되어 빌드 → ECR → EC2 재기동까지 돕니다.
> 블루그린 미적용이라 재시작 구간에 502가 나가므로, 문서 수정이라도 `main`에 직접 push하지 마세요.

---

## 🔀 Commit Rule

```text
<commit-type>: (#이슈번호) <subject>
```

* subject는 **한국어**로, 무엇을 했는지 한 줄
* **제목만 씁니다.** 본문(description)은 비웁니다

```bash
git commit -m "feat: (#1059) 엔티티에 previousStatus 컬럼 추가"
git commit -m "fix: (#1032) fcm 설정 yml파일에 추가"
git commit -m "test: (#1059) 이전 상태로 돌아가는 로직으로 테스트 코드 수정"
git commit -m "docs: (#1080) 온보딩 문서 추가"
```

### Commit Type

| type | 용도 |
| --- | --- |
| `feat` | 새로운 기능 추가 |
| `fix` | 버그 수정 |
| `refactor` | 기능 변화 없는 코드 개선 |
| `test` | 테스트 추가·수정 |
| `style` | 포맷·detekt 등 스타일 변경 |
| `chore` | 빌드·설정·CI 등 기타 |
| `docs` | 문서 |
| `merge` | develop 머지 커밋 |

여러 커밋을 모아 develop에 머지할 때는 `merge: (#이슈번호) 요약` 형태를 씁니다.

push하면 pre-push 훅이 detekt를 돌립니다. 훅이 안 걸려 있다면 한 번만 연결하세요.

```bash
git config core.hooksPath .githooks
```

---

## 🔁 Pull Request

base는 **`develop`**입니다. 템플릿(`.github/PULL_REQUEST_TEMPLATE.md`)을 그대로 채웁니다.

```md
## 작업 내용 설명
## 주요 변경 사항
## 결과물
## 체크리스트
- [ ] 어플리케이션 구동(혹은 테스트)시 오류는 없나요?
- [ ] 생성된 코드에 Javadoc 주석을 추가 하였나요?
- [ ] 생성된 코드에 대한 테스트 코드가 작성 되었나요?
## 관련 이슈
- resolved #<이슈번호>
```

### 머지 전 통과해야 하는 것

* `build`(전체 gradle build), `CI`(테스트 + 커버리지 코멘트)
* CodeRabbit 리뷰

> 리뷰 지적을 **근거를 대고 반려하는 것도 정상 대응**입니다. 단 근거는 비즈니스 규칙이나 실측이어야 합니다.

---

## 전체 흐름

```text
이슈 생성
   ↓
develop에서 <type>/<이슈번호>-<작업내용> 브랜치 생성
   ↓
커밋:  <type>: (#이슈번호) 설명
   ↓
push  →  pre-push 훅이 detekt 실행
   ↓
develop으로 PR  →  CI + CodeRabbit 통과  →  머지
   ↓
배포할 때 develop을 main에 머지
```
