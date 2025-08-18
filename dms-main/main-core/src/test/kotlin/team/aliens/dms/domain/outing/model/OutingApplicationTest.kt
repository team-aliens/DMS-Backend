package team.aliens.dms.domain.outing.model

import io.kotest.assertions.throwables.shouldNotThrowAny
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.DescribeSpec
import team.aliens.dms.domain.outing.exception.OutingTypeMismatchException
import team.aliens.dms.domain.outing.stub.createOutingApplicationStub

class OutingApplicationTest : DescribeSpec({

    describe("checkCancelable") {
        context("외출 신청 상태가 REQUESTED이면") {
            val outingApplication = createOutingApplicationStub(isApproved = false)

            it("외출 취소가 가능하다") {
                shouldNotThrowAny {
                    outingApplication.checkCancelable(false)
                }
            }
        }

        context("외출 신청 상태가 REQUESTED가 아니면") {
            val outingApplication = createOutingApplicationStub(isApproved = true)

            it("외출 취소가 불가능하다") {
                shouldThrow<OutingTypeMismatchException> {
                    outingApplication.checkCancelable(true)
                }
            }
        }
    }
})
