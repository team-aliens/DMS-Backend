package team.aliens.dms.domain.volunteer.model

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe

class VolunteerApplicationStatusTest : DescribeSpec({

    describe("of") {
        context("isApproved가 true이면") {
            it("APPLIED 상태를 반환한다") {
                VolunteerApplicationStatus.of(true) shouldBe VolunteerApplicationStatus.APPLIED
            }
        }

        context("isApproved가 false이면") {
            it("APPLYING 상태를 반환한다") {
                VolunteerApplicationStatus.of(false) shouldBe VolunteerApplicationStatus.APPLYING
            }
        }

        context("isApproved가 null이면") {
            it("NOT_APPLIED 상태를 반환한다") {
                VolunteerApplicationStatus.of(null) shouldBe VolunteerApplicationStatus.NOT_APPLIED
            }
        }
    }
})
