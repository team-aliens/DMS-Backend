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
hotfix/1096-log-error
```

### 브랜치 구조

```text
main            운영 배포 브랜치
 └── develop    기본 개발 브랜치
       ├── feat/1059-revert-application
       └── fix/1074-general-teacher-sort
```

* 작업 브랜치는 **`develop`에서 따고 `develop`으로 PR**합니다
* `main`은 `develop`을 머지해 배포할 때만 갱신합니다 — 이것도 **PR**로 합니다(아래 배포 절차)

### 브랜치 보호

**`main`·`develop`은 직접 push가 막혀 있습니다. 관리자도 예외 없습니다.** 두 브랜치에 들어가는 모든 변경은 PR을 거칩니다.

| | `main` | `develop` |
| --- | --- | --- |
| 직접 push | ✗ | ✗ |
| PR 필수 + 리뷰 1 + `build` 체크 | ✓ | ✓ |
| force push · 브랜치 삭제 | ✗ | ✗ |

* 잘못 머지된 걸 되돌릴 때도 force push가 아니라 **revert PR**입니다
* PR 머지 후 "Delete branch"는 **작업 브랜치에만** 누릅니다. 릴리스·역머지 PR은 head가 `develop`/`main`이라 브랜치가 통째로 지워집니다(2026-09-15 #1099 머지 직후 `main`이 삭제돼 복구한 적 있음)

### Hotfix

운영에서 급한 버그는 `develop`을 거치지 않고 바로 배포합니다.

1. `main`에서 `hotfix/<이슈번호>-<작업내용>` 브랜치를 따고 **`main`으로 PR**
2. 머지 후 태그 → Release publish로 배포
3. **`main` → `develop` 역머지 PR**을 올립니다(head=`main`, base=`develop`). 안 하면 다음 릴리스 때까지 `develop`에 수정이 없고, 그 사이 `develop`에서 딴 브랜치는 버그를 그대로 갖고 갑니다

> ⚠️ **운영 배포는 GitHub Release를 publish할 때 실행됩니다.** `main` push나 태그 push만으로는 배포되지 않습니다.
> `prod-cd.yml`이 릴리스 태그를 커밋으로 확정한 뒤 테스트 → 이미지 빌드/ECR → EC2 재기동까지 돕니다.

**배포 절차**

1. **`develop` → `main` PR**을 올려 머지합니다. 제목은 `merge: (#이슈번호) 요약` 형식, 머지 방식은 **Create a merge commit**
2. 배포할 `main` 커밋에 태그(`vX.Y.Z`)를 만들어 push합니다
3. 그 태그로 **GitHub Release를 publish**합니다 → 여기서 배포가 시작됩니다

* 프리릴리스로 표시한 Release는 자동 배포되지 않습니다
* **롤백**은 Actions에서 `CD for Prod`를 수동 실행(`workflow_dispatch`)하고, 되돌릴 릴리스 태그를 입력합니다.
  이미 존재하는 릴리스 태그만 받습니다(브랜치명·임의 커밋은 거부).

> 블루그린 미적용이라 재시작 구간에 502가 나갑니다.

> ⚠️ **릴리스 PR과 역머지 PR은 반드시 merge commit으로 머지합니다.** Squash나 Rebase로 머지하면 같은 변경이 `main`과 `develop`에 서로 다른 커밋으로 남아, 이후 두 브랜치를 머지할 때마다 "상대 브랜치에 없는 커밋"이 계속 따라다닙니다.

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
| `perf` | 성능 개선 |
| `merge` | PR 머지 커밋 (PR 제목이 그대로 머지 커밋 메시지가 됩니다) |

PR 제목은 `merge: (#이슈번호) 요약` 형태를 씁니다. 릴리스·역머지 PR도 같습니다(예: `merge: (#1096) hotfix 역머지 main → develop`).

### 로컬 브랜치 만들 때

```bash
git switch develop && git pull
git switch -c feat/1234-something        # 로컬 develop에서 딴다
git push -u origin feat/1234-something   # 첫 push에 -u로 같은 이름의 upstream 등록
```

* **원격 브랜치(`origin/main`, `origin/develop` 등)를 시작점으로 브랜치를 만들지 마세요.** git이 그 원격 브랜치를 upstream으로 자동 등록해서, GitKraken·IntelliJ의 Push 버튼이 묻지 않고 **그 원격 브랜치로 push**합니다(2026-09-15에 `hotfix` 브랜치가 `origin/main`을 추적하고 있어 `main`에 세 번 오발 push된 원인)
* 이미 만든 브랜치는 `git branch -vv`로 upstream을 확인하고, 잘못됐으면 `git branch --unset-upstream` 후 `-u`로 다시 push
* CLI에서는 `git config --global push.default current`를 걸어두면 upstream과 무관하게 항상 같은 이름으로만 push됩니다

push하면 pre-push 훅이 detekt를 돌립니다. 훅이 안 걸려 있다면 한 번만 연결하세요.

```bash
git config core.hooksPath .githooks
```

---

## 🔁 Pull Request

base는 **`develop`**입니다. 예외는 릴리스 PR(`develop` → `main`), hotfix PR(`hotfix/*` → `main`), 역머지 PR(`main` → `develop`) 세 가지뿐입니다. 템플릿(`.github/PULL_REQUEST_TEMPLATE.md`)을 그대로 채웁니다.

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
배포할 때 develop → main PR 머지(merge commit)  →  태그 push  →  GitHub Release publish  →  운영 배포
   ↓
hotfix였다면 main → develop 역머지 PR
```
