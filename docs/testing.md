# 테스트 가이드

테스트를 **어디에, 어떤 형식으로 쓰는지** 정리한 문서입니다.

---

## 어디에 작성하나

**테스트는 `main-core`에 작성합니다.**

```
main-core/src/test/kotlin/team/aliens/dms/domain/<도메인>/
├── model/     XxxTest.kt              도메인 모델 (상태 전이·불변식)
├── usecase/   XxxUseCaseTest.kt       유스케이스
├── service/   XxxServiceImplTest.kt   서비스
└── stub/      XxxStub.kt              테스트용 스텁 팩토리
```

- 테스트 클래스는 **대상과 같은 패키지**에 `<대상클래스>Test`로 만듭니다
- 비즈니스 로직이 전부 core에 있으므로 검증도 여기서 합니다
- `main-infrastructure`에 JWT·필터·어댑터 테스트가 소수 있지만 예외적인 경우입니다. `main-persistence`·`main-presentation`에는 테스트가 없습니다

---

## 실행

```bash
./gradlew check      # 전 모듈 test + detekt — CI가 배포 전에 도는 것
./gradlew :dms-main:main-core:test
./gradlew detekt
```

> **`./gradlew :dms-main:test`는 테스트를 하나도 돌리지 않습니다.** `dms-main`은 껍데기라 `NO-SOURCE`로 통과합니다. `check`를 쓰세요.
>

> 결과를 파이프로 넘기면 종료코드가 뒤쪽 명령의 것이 잡혀 **실패가 성공처럼 보입니다.**
>

Testcontainers를 쓰는 테스트가 있으므로 Docker가 떠 있어야 합니다.

---

## 기본 양식

**Kotest `DescribeSpec` + MockK**, 설명은 **한국어 문장형**입니다.

```kotlin
class RevertXxxUseCaseTest : DescribeSpec({

    // 1. 협력 객체는 mockk로, 대상은 실제 객체로 만든다
    val xxxService = mockk<XxxService>()
    val securityService = mockk<SecurityService>()

    val useCase = RevertXxxUseCase(xxxService, securityService)

    // 2. 테스트마다 mock 상태를 초기화한다
    beforeTest {
        clearMocks(xxxService, securityService)
    }

    // 3. describe = 대상 메서드명
    describe("execute") {

        // 4. context = 입력·상태 조건
        context("최종 승인(SECOND_APPROVED)한 신청이라면") {

            // 5. it = 그때 기대하는 결과
            it("직전 상태로 되돌려 저장한다") {
                val schoolId = UUID.randomUUID()
                val applications = listOf(
                    createXxxStub(status = Status.SECOND_APPROVED, previousStatus = Status.FIRST_APPROVED)
                )

                every { securityService.getCurrentSchoolId() } returns schoolId
                every { xxxService.getAllByIdIn(any()) } returns applications
                every { xxxService.saveAll(any()) } just runs

                useCase.execute(RevertXxxRequest(ids))

                applications[0].status shouldBe Status.FIRST_APPROVED
                verify(exactly = 1) { xxxService.saveAll(any()) }
            }
        }

        context("되돌릴 수 없는 상태의 신청이 포함되면") {

            it("XxxCanNotRevertException을 던지고 저장하지 않는다") {
                // ...
                shouldThrow<XxxCanNotRevertException> { useCase.execute(request) }
                verify(exactly = 0) { xxxService.saveAll(any()) }
            }
        }
    }
})
```

| 블록 | 무엇을 적나 | 예 |
| --- | --- | --- |
| `describe` | 대상 메서드명 | `"execute"`, `"revert"`, `"subscribeTopic"` |
| `context` | 입력·상태 조건. **"~면", "~이라면"** | `"존재하지 않는 신청 id가 포함되면"` |
| `it` | 기대 결과. **"~한다", "~을 던진다"** | `"직전 상태로 되돌려 저장한다"` |

---

## 규칙

### 스텁과 호출은 `it` 안에

`describe`/`context` 본문은 **테스트 등록 시점에 실행됩니다.**

- 대상 호출을 거기 두면 실패가 특정 테스트에 안 붙고 `initializationError`로 뭉개집니다
- 스텁을 거기 두면 다른 `context`의 스텁이 서로를 덮어씁니다 (특히 인자 없는 같은 메서드)

> 오래된 테스트는 `context` 본문에 `every`를 두는 스타일입니다. **새 테스트는 따라 하지 마세요.**
>

### mock 격리

위 양식처럼 spec 최상단에 만들고 `beforeTest { clearMocks(...) }`로 초기화하는 것이 기본입니다.
`confirmVerified`나 `verify(exactly = 0)`을 쓴다면 **범위를 좁혀 새로 만드세요.** 공유하면 이전 테스트의 호출까지 세게 됩니다.

### 표 기반 테스트

같은 로직을 값만 바꿔 검증할 땐 `context`를 복붙하지 말고 `forAll` + `row`로 묶습니다.

```kotlin
it("XxxCanNotRevertException을 던지고 저장하지 않는다") {
    forAll(
        row(Status.PENDING),
        row(Status.FIRST_APPROVED),
        row(Status.EXPIRED),
    ) { status ->
        // ...
        clearAllMocks()   // ← row 끝마다 직접
    }
}
```