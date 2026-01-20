package team.aliens.dms.domain.volunteer.model

import io.kotest.assertions.throwables.shouldNotThrowAny
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.DescribeSpec
import team.aliens.dms.domain.volunteer.exception.VolunteerApplicationAlreadyAssigned
import team.aliens.dms.domain.volunteer.exception.VolunteerApplicationNotAssigned
import team.aliens.dms.domain.volunteer.stub.createVolunteerApplicationStub

class VolunteerApplicationTest : DescribeSpec({

    describe("checkIsNotApproved") {
        context("승인되지 않은 신청이면") {
            val application = createVolunteerApplicationStub(approved = false)

            it("예외가 발생하지 않는다") {
                shouldNotThrowAny {
                    application.checkIsNotApproved()
                }
            }
        }

        context("이미 승인된 신청이면") {
            val application = createVolunteerApplicationStub(approved = true)

            it("VolunteerApplicationAlreadyAssigned를 발생시킨다") {
                shouldThrow<VolunteerApplicationAlreadyAssigned> {
                    application.checkIsNotApproved()
                }
            }
        }
    }

    describe("checkIsApproved") {
        context("승인된 신청이면") {
            val application = createVolunteerApplicationStub(approved = true)

            it("예외가 발생하지 않는다") {
                shouldNotThrowAny {
                    application.checkIsApproved()
                }
            }
        }

        context("승인되지 않은 신청이면") {
            val application = createVolunteerApplicationStub(approved = false)

            it("VolunteerApplicationNotAssigned를 발생시킨다") {
                shouldThrow<VolunteerApplicationNotAssigned> {
                    application.checkIsApproved()
                }
            }
        }
    }
})
