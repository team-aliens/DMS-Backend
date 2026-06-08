package team.aliens.dms.domain.daybreak.model

import io.kotest.assertions.throwables.shouldNotThrowAny
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockkStatic
import io.mockk.unmockkStatic
import team.aliens.dms.domain.auth.model.Authority
import team.aliens.dms.domain.daybreak.exception.DaybreakInvalidDateRangeException
import team.aliens.dms.domain.daybreak.exception.DaybreakPastDateException
import team.aliens.dms.domain.daybreak.exception.DaybreakStartDateAfterEndDateException
import team.aliens.dms.domain.daybreak.stub.createDaybreakStudyApplicationStub
import team.aliens.dms.domain.user.exception.InvalidRoleException
import java.time.LocalDate
import java.util.UUID

class DaybreakStudyApplicationTest : DescribeSpec({

    describe("create") {

        fun create(today: LocalDate, startDate: LocalDate, endDate: LocalDate) {
            mockkStatic(LocalDate::class)
            every { LocalDate.now() } returns today
            try {
                DaybreakStudyApplication.create(
                    studyTypeId = UUID.randomUUID(),
                    startDate = startDate,
                    endDate = endDate,
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

        context("월요일에 이번 주 월~목 범위로 신청하면") {
            val monday = LocalDate.of(2025, 6, 2)
            it("정상적으로 생성된다") {
                shouldNotThrowAny { create(today = monday, startDate = monday, endDate = monday.plusDays(3)) }
            }
        }

        context("목요일에 이번 주 목요일 하루만 신청하면") {
            val thursday = LocalDate.of(2025, 6, 5)
            it("정상적으로 생성된다") {
                shouldNotThrowAny { create(today = thursday, startDate = thursday, endDate = thursday) }
            }
        }

        context("금요일에 다음 주 월~목으로 신청하면") {
            val friday = LocalDate.of(2025, 6, 6)
            val nextMonday = LocalDate.of(2025, 6, 9)
            val nextThursday = LocalDate.of(2025, 6, 12)
            it("정상적으로 생성된다") {
                shouldNotThrowAny { create(today = friday, startDate = nextMonday, endDate = nextThursday) }
            }
        }

        context("토요일에 다음 주 월~목으로 신청하면") {
            val saturday = LocalDate.of(2025, 6, 7)
            val nextMonday = LocalDate.of(2025, 6, 9)
            val nextThursday = LocalDate.of(2025, 6, 12)
            it("정상적으로 생성된다") {
                shouldNotThrowAny { create(today = saturday, startDate = nextMonday, endDate = nextThursday) }
            }
        }

        context("일요일에 다음 주 월~목으로 신청하면") {
            val sunday = LocalDate.of(2025, 6, 8)
            val nextMonday = LocalDate.of(2025, 6, 9)
            val nextThursday = LocalDate.of(2025, 6, 12)
            it("정상적으로 생성된다") {
                shouldNotThrowAny { create(today = sunday, startDate = nextMonday, endDate = nextThursday) }
            }
        }

        context("금요일에 이번 주 목요일(과거)로 신청하면") {
            val friday = LocalDate.of(2025, 6, 6)
            val thisThursday = LocalDate.of(2025, 6, 5)
            it("DaybreakPastDateException을 던진다") {
                shouldThrow<DaybreakPastDateException> {
                    create(today = friday, startDate = thisThursday, endDate = thisThursday)
                }
            }
        }

        context("금요일에 다음 주 금요일로 신청하면") {
            val friday = LocalDate.of(2025, 6, 6)
            val nextFriday = LocalDate.of(2025, 6, 13)
            it("DaybreakInvalidDateRangeException을 던진다") {
                shouldThrow<DaybreakInvalidDateRangeException> {
                    create(today = friday, startDate = nextFriday, endDate = nextFriday)
                }
            }
        }

        context("startDate가 endDate보다 늦으면") {
            val monday = LocalDate.of(2025, 6, 2)
            it("DaybreakStartDateAfterEndDateException을 던진다") {
                shouldThrow<DaybreakStartDateAfterEndDateException> {
                    create(today = monday, startDate = monday.plusDays(2), endDate = monday)
                }
            }
        }

        context("과거 날짜로 신청하면") {
            val wednesday = LocalDate.of(2025, 6, 4)
            it("DaybreakPastDateException을 던진다") {
                shouldThrow<DaybreakPastDateException> {
                    create(today = wednesday, startDate = wednesday.minusDays(1), endDate = wednesday)
                }
            }
        }
    }

    describe("changeApplicationStatus") {

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

            it("허용되지 않은 권한(예: STUDENT)으로 변경을 시도하면 예외가 발생한다") {
                val application = createDaybreakStudyApplicationStub(status = Status.PENDING)
                shouldThrow<InvalidRoleException> {
                    application.changeStatus(Authority.STUDENT, Status.FIRST_APPROVED)
                }
            }
        }
    }
})
