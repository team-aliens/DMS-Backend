# 코드 컨벤션

Kotlin 코드 스타일 규칙입니다. 모듈 구조·네이밍·검증 위치·보안·동시성은 [architecture.md](./architecture.md), 테스트는 [testing.md](./testing.md), 스키마·QueryDSL은 [database.md](./database.md), 브랜치·커밋은 [git-convention.md](./git-convention.md)에 있습니다.

---

## 도메인 모델

기준은 **모델이 자기 상태를 바꾸는 메서드를 갖는가** 하나입니다.

| 상태 전이 | 클래스 | 상태 필드 | 갱신 |
| --- | --- | --- | --- |
| 있음 | 일반 `class` | body 프로퍼티 `var` + `private set` | 모델 메서드가 제자리 변경 |
| 없음 | `data class` | 주생성자 `val` | `copy()`로 새 인스턴스 |

현재 전자는 `DaybreakStudyApplication` 하나입니다. 나머지는 전부 후자이고 그대로 둡니다.

### 상태 전이가 있는 모델

```kotlin
@Aggregate
class DaybreakStudyApplication(
    status: Status,                  // val/var 없는 파라미터로 받고
    // ...
) : SchoolIdDomain {

    var status: Status = status      // body에서 프로퍼티로 선언
        private set

    fun changeStatus(authority: Authority, newStatus: Status) {
        // 권한·상태 전이 검증 후
        this.previousStatus = this.status
        this.status = newStatus
    }
}
```

* 읽기는 공개, 쓰기는 모델 안에서만. **상태를 바꾸는 경로가 필요하면 전이 메서드를 추가하세요** — 필드에 직접 대입하면 검증과 이력 기록이 통째로 우회됩니다(스케줄러 만료도 `expire()` 메서드로 둡니다).
* **`private set`은 주생성자 파라미터에 못 붙입니다.** 위처럼 body 프로퍼티로 내려야 하고, `data class`는 주생성자가 전부 `val`/`var`여야 해서 이 형태 자체가 불가능합니다.
* **`copy()`를 반환하는 방식은 쓰지 않습니다.** 호출부가 반환값을 빠뜨리면 경고 없이 no-op이 되는데 컴파일러·detekt·유스케이스 테스트 어느 것도 못 잡습니다.
* **`equals`/`hashCode`는 재정의하지 않습니다(identity 비교).** JPA 엔티티도 전부 그렇고, 미저장 인스턴스는 `id`가 `UUID(0, 0)`이라 id 기준 `equals`는 오히려 틀립니다.

### 상태 전이가 없는 모델

```kotlin
data class NotificationOfUser(val isRead: Boolean = false /* ... */) {
    fun read() = this.copy(isRead = true)
}
```

값을 외부에 노출하지 않고 판단만 시키려면 `private val`을 씁니다(`Notification.isSaveRequired` + `runIfSaveRequired`).

---

## 존재/중복 검사

`CheckXxxService` + `CheckXxxServiceImpl`에 두고, 위반이면 throw·반환은 `Unit`. 유스케이스는 호출만 하고 happy path로 진행합니다.

```kotlin
override fun checkDaybreakStudyTypeExists(schoolId: UUID, name: String) {
    if (queryDaybreakStudyTypePort.existsDaybreakStudyTypeBySchoolIdAndName(schoolId, name)) {
        throw DaybreakStudyTypeAlreadyExistsException
    }
}
```

분기가 필요할 때만 `Boolean` 반환 변형을 씁니다. 검사 종류별 위치는 [architecture.md](./architecture.md)를 따르세요.

---

## 예외

* 이름은 조건이 아니라 **막힌 액션**으로 — `XxxCanNotYyyException`. 조건(상태값)을 이름에 넣으면 정책이 바뀔 때 이름이 거짓말이 됩니다.
* `ErrorCode`의 `sequence`는 수기 관리라 같은 `ErrorStatus` 그룹에서 겹치기 쉽습니다. 추가 전에 그룹 번호를 훑으세요.
* 리스트 요청 DTO는 `@field:NotNull`이 아니라 **`@field:NotEmpty`** — `[]`가 통과해 조용히 no-op이 됩니다.
* 페이지네이션을 붙일 땐 기존 `isEmpty() → NotFoundException` 가드를 지우세요. 마지막 페이지 다음이 404가 됩니다.

---

## 알림(FCM)

저장만 `runIfSaveRequired { }`로 감싸고 FCM 발송은 게이트 밖에 둡니다. 덕분에 한쪽만 끄는 게 한 줄입니다.

| `isSaveRequired` | 푸시 | 알림함 |
| --- | --- | --- |
| `true` | 발송 | 적재 |
| `false` | 발송 | 안 남음 |

`Topic` enum 값을 추가할 땐 이름 길이를 `VARCHAR(n)`과 대조하세요 — `ddl-auto: validate`는 길이를 안 봅니다([database.md](./database.md)).

---

## 로컬 빌드

* 시그니처를 바꾼 뒤 이상한 타입 에러가 나면 clean 빌드부터 — 증분 컴파일 캐시가 옛 시그니처를 뭅니다.
* 파일마다 개행이 섞여 있습니다(CRLF/LF). 스크립트로 치환했으면 `git diff --stat`의 줄 수를 확인하세요.
* `./gradlew ... | tail`은 종료코드가 가려져 실패가 성공으로 보입니다.
