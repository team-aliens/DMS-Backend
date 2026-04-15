package team.aliens.dms.domain.daybreak.model

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import team.aliens.dms.domain.auth.model.Authority
import team.aliens.dms.domain.daybreak.stub.createDaybreakStudyApplicationStub
import team.aliens.dms.domain.user.exception.InvalidRoleException

class DaybreakStudyApplicationTest : DescribeSpec({

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
