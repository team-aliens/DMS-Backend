package team.aliens.dms.domain.daybreak.model

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.data.forAll
import io.kotest.data.row
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockkStatic
import io.mockk.unmockkStatic
import team.aliens.dms.domain.auth.model.Authority
import team.aliens.dms.domain.daybreak.exception.DaybreakEmptyDateException
import team.aliens.dms.domain.daybreak.exception.DaybreakInvalidDateRangeException
import team.aliens.dms.domain.daybreak.exception.DaybreakPastDateException
import team.aliens.dms.domain.daybreak.exception.DaybreakStudyApplicationCanNotRevertException
import team.aliens.dms.domain.daybreak.stub.createDaybreakStudyApplicationStub
import team.aliens.dms.domain.user.exception.InvalidRoleException
import java.time.LocalDate
import java.util.UUID

class DaybreakStudyApplicationTest : DescribeSpec({

    describe("create") {

        fun create(today: LocalDate, dates: List<LocalDate>): DaybreakStudyApplication {
            mockkStatic(LocalDate::class)
            every { LocalDate.now() } returns today
            try {
                return DaybreakStudyApplication.create(
                    studyTypeId = UUID.randomUUID(),
                    dates = dates,
                    reason = "신청합니다",
                    status = Status.PENDING,
                    teacherId = UUID.randomUUID(),
                    studentId = UUID.randomUUID(),
                    schoolId = UUID.randomUUID()
                )
            } finally {
                unmockkStatic(LocalDate::class)
            }
        }

        val monday = LocalDate.of(2025, 6, 2)
        val tuesday = LocalDate.of(2025, 6, 3)
        val wednesday = LocalDate.of(2025, 6, 4)
        val thursday = LocalDate.of(2025, 6, 5)
        val friday = LocalDate.of(2025, 6, 6)
        val saturday = LocalDate.of(2025, 6, 7)
        val sunday = LocalDate.of(2025, 6, 8)
        val nextMonday = LocalDate.of(2025, 6, 9)
        val nextThursday = LocalDate.of(2025, 6, 12)
        val nextFriday = LocalDate.of(2025, 6, 13)

        context("월요일에 이번 주 월~목을 모두 신청하면") {
            it("고른 날짜를 모두 가진다") {
                val application = create(today = monday, dates = listOf(monday, tuesday, wednesday, thursday))

                application.dates shouldBe listOf(monday, tuesday, wednesday, thursday)
            }
        }

        context("중간에 쉬는 날을 두고 월·수만 신청하면") {
            it("고른 날짜만 가진다") {
                val application = create(today = monday, dates = listOf(monday, wednesday))

                application.dates shouldBe listOf(monday, wednesday)
                application.startDate shouldBe monday
                application.endDate shouldBe wednesday
            }
        }

        context("날짜를 순서 없이, 중복해서 보내면") {
            it("중복을 합치고 오름차순으로 정렬한다") {
                val application = create(today = monday, dates = listOf(thursday, monday, thursday))

                application.dates shouldBe listOf(monday, thursday)
            }
        }

        context("목요일에 이번 주 목요일 하루만 신청하면") {
            it("목요일 하루만 가진다") {
                val application = create(today = thursday, dates = listOf(thursday))

                application.dates shouldBe listOf(thursday)
            }
        }

        context("금~일에 다음 주 월·목을 신청하면") {
            it("다음 주 월·목을 가진다") {
                forAll(
                    row(friday),
                    row(saturday),
                    row(sunday),
                ) { today ->
                    val application = create(today = today, dates = listOf(nextMonday, nextThursday))

                    application.dates shouldBe listOf(nextMonday, nextThursday)
                }
            }
        }

        context("날짜 없이 신청하면") {
            it("DaybreakEmptyDateException을 던진다") {
                shouldThrow<DaybreakEmptyDateException> {
                    create(today = monday, dates = emptyList())
                }
            }
        }

        context("금요일에 이번 주 목요일(과거)로 신청하면") {
            it("DaybreakPastDateException을 던진다") {
                shouldThrow<DaybreakPastDateException> {
                    create(today = friday, dates = listOf(thursday))
                }
            }
        }

        context("고른 날짜 중 하나라도 과거면") {
            it("DaybreakPastDateException을 던진다") {
                shouldThrow<DaybreakPastDateException> {
                    create(today = wednesday, dates = listOf(tuesday, thursday))
                }
            }
        }

        context("고른 날짜 중 하나라도 대상 주의 월~목 밖이면") {
            it("DaybreakInvalidDateRangeException을 던진다") {
                forAll(
                    row(listOf(nextFriday)),
                    row(listOf(nextMonday, nextFriday)),
                    row(listOf(monday, nextMonday)),
                ) { dates ->
                    shouldThrow<DaybreakInvalidDateRangeException> {
                        create(today = monday, dates = dates)
                    }
                }
            }
        }
    }

    describe("changeStatus") {

        context("담당 선생님(GENERAL_TEACHER)이 변경할 때 ") {
            it("PENDING 상태에서 FIRST_APPROVED 상태로 변경 가능하다") {
                val application = createDaybreakStudyApplicationStub(status = Status.PENDING)
                application.changeStatus(Authority.GENERAL_TEACHER, Status.FIRST_APPROVED)
                application.status shouldBe Status.FIRST_APPROVED
            }

            it("PENDING 상태에서 REJECTED 상태로 변경 가능하다") {
                val application = createDaybreakStudyApplicationStub(status = Status.PENDING)
                application.changeStatus(Authority.GENERAL_TEACHER, Status.REJECTED)
                application.status shouldBe Status.REJECTED
            }

            it("PENDING 상태가 아니라면 예외를 던진다") {
                val application = createDaybreakStudyApplicationStub(status = Status.FIRST_APPROVED)
                shouldThrow<InvalidRoleException> {
                    application.changeStatus(Authority.GENERAL_TEACHER, Status.SECOND_APPROVED)
                }
            }

            it("FIRST_APPROVED 상태나 REJECTED 상태가 아닌 다른 상태로의 변경은 불가능하다") {
                val application = createDaybreakStudyApplicationStub(status = Status.PENDING)
                shouldThrow<InvalidRoleException> {
                    application.changeStatus(Authority.GENERAL_TEACHER, Status.SECOND_APPROVED)
                }
            }
        }

        context("부장 선생님(HEAD_TEACHER)이 변경할 때") {
            it("FIRST_APPROVED 상태에서 SECOND_APPROVED 상태로 변경 가능하다") {
                val application = createDaybreakStudyApplicationStub(status = Status.FIRST_APPROVED)
                application.changeStatus(Authority.HEAD_TEACHER, Status.SECOND_APPROVED)
                application.status shouldBe Status.SECOND_APPROVED
            }

            it("FIRST_APPROVED 상태에서 REJECTED 상태로 변경 가능하다") {
                val application = createDaybreakStudyApplicationStub(status = Status.FIRST_APPROVED)
                application.changeStatus(Authority.HEAD_TEACHER, Status.REJECTED)
                application.status shouldBe Status.REJECTED
            }

            it("FIRST_APPROVED 상태가 아닐 때 변경을 시도하면 예외가 발생한다") {
                val application = createDaybreakStudyApplicationStub(status = Status.PENDING)
                shouldThrow<InvalidRoleException> {
                    application.changeStatus(Authority.HEAD_TEACHER, Status.SECOND_APPROVED)
                }
            }
        }

        context("공통 예외 케이스") {
            it("REJECTED 상태인 경우 어떤 권한으로도 변경할 수 없다") {
                val application = createDaybreakStudyApplicationStub(status = Status.REJECTED)
                shouldThrow<InvalidRoleException> {
                    application.changeStatus(Authority.HEAD_TEACHER, Status.SECOND_APPROVED)
                }
            }

            it("SECOND_APPROVED 상태인 경우 어떤 권한으로도 변경할 수 없다") {
                val application = createDaybreakStudyApplicationStub(status = Status.SECOND_APPROVED)
                shouldThrow<InvalidRoleException> {
                    application.changeStatus(Authority.GENERAL_TEACHER, Status.FIRST_APPROVED)
                }
            }

            it("상태를 변경하면 직전 상태를 기록한다") {
                val application = createDaybreakStudyApplicationStub(status = Status.PENDING)
                application.changeStatus(Authority.GENERAL_TEACHER, Status.FIRST_APPROVED)
                application.previousStatus shouldBe Status.PENDING

                application.changeStatus(Authority.HEAD_TEACHER, Status.SECOND_APPROVED)
                application.previousStatus shouldBe Status.FIRST_APPROVED
            }

            it("허용되지 않은 권한(예: STUDENT)으로 변경을 시도하면 예외가 발생한다") {
                val application = createDaybreakStudyApplicationStub(status = Status.PENDING)
                shouldThrow<InvalidRoleException> {
                    application.changeStatus(Authority.STUDENT, Status.FIRST_APPROVED)
                }
            }
        }
    }

    describe("revert") {

        it("직전 상태로 되돌린다") {
            forAll(
                row(Status.SECOND_APPROVED, Status.FIRST_APPROVED),
                row(Status.REJECTED, Status.FIRST_APPROVED),
                row(Status.REJECTED, Status.PENDING),
            ) { status, previousStatus ->
                val application = createDaybreakStudyApplicationStub(
                    status = status,
                    previousStatus = previousStatus
                )

                application.revert()

                application.status shouldBe previousStatus
            }
        }

        it("되돌린 뒤에는 직전 상태가 비어 다시 되돌릴 수 없다") {
            val application = createDaybreakStudyApplicationStub(
                status = Status.SECOND_APPROVED,
                previousStatus = Status.FIRST_APPROVED
            )

            application.revert()

            application.previousStatus shouldBe null
            shouldThrow<DaybreakStudyApplicationCanNotRevertException> {
                application.revert()
            }
        }

        it("직전 상태가 없으면 DaybreakStudyApplicationCanNotRevertException을 던진다") {
            forAll(
                row(Status.SECOND_APPROVED),
                row(Status.REJECTED),
            ) { status ->
                val application = createDaybreakStudyApplicationStub(status = status, previousStatus = null)
                shouldThrow<DaybreakStudyApplicationCanNotRevertException> {
                    application.revert()
                }
            }
        }

        it("되돌릴 수 없는 상태면 DaybreakStudyApplicationCanNotRevertException을 던진다") {
            forAll(
                row(Status.PENDING),
                row(Status.FIRST_APPROVED),
                row(Status.EXPIRED),
            ) { status ->
                val application = createDaybreakStudyApplicationStub(
                    status = status,
                    previousStatus = Status.FIRST_APPROVED
                )
                shouldThrow<DaybreakStudyApplicationCanNotRevertException> {
                    application.revert()
                }
            }
        }
    }
})
