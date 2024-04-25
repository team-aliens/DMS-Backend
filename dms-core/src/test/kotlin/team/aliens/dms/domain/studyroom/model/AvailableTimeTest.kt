package team.aliens.dms.domain.studyroom.model

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import team.aliens.dms.domain.studyroom.stub.createAvailableTime
import java.time.LocalTime

class AvailableTimeTest : DescribeSpec({

    describe("isAvailable") {
        val availableTime = createAvailableTime(
            startAt = LocalTime.of(20, 30, 0),
            endAt = LocalTime.of(23, 50, 0)
        )

        context("자습실을 신청한 시간이 자습실 신청 가능 시간이라면") {
            val isAvailable = availableTime.isAvailable(LocalTime.of(20, 30, 0))

            it("자습실 신청이 가능하다") {
                isAvailable shouldBe true
            }
        }

        context("자습실을 신청한 시간이 자습실 신청 가능 시간 이전이면") {
            val isAvailable = availableTime.isAvailable(LocalTime.of(20, 29, 59))

            it("자습실 신청이 불가능하다") {
                isAvailable shouldBe false
            }
        }

        context("자습실을 신청한 시간이 자습실 신청 가능 시간 이후이면") {
            val isAvailable = availableTime.isAvailable(LocalTime.of(23, 50, 1))

            it("자습실 신청이 불가능하다") {
                isAvailable shouldBe false
            }
        }
    }
})
