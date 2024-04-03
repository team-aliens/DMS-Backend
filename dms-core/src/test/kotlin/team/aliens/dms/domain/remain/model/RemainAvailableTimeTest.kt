package team.aliens.dms.domain.remain.model

import io.kotest.assertions.throwables.shouldNotThrowAny
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.DescribeSpec
import team.aliens.dms.domain.remain.exception.RemainCanNotAppliedException
import team.aliens.dms.domain.remain.stub.createRemainAvailableTimeStub
import java.time.DayOfWeek
import java.time.LocalDateTime
import java.time.LocalTime

class RemainAvailableTimeTest : DescribeSpec({

    describe("checkAvailable") {
        val remainAvailableTime = createRemainAvailableTimeStub(
            startDayOfWeek = DayOfWeek.THURSDAY,
            startTime = LocalTime.of(8, 30, 0),
            endDayOfWeek = DayOfWeek.THURSDAY,
            endTime = LocalTime.of(10, 0, 0)
        )

        context("잔류 신청한 시간이 잔류 신청 가능 시간이면") {
            it("잔류 신청이 가능하다") {
                shouldNotThrowAny {
                    remainAvailableTime.checkAvailable(
                        LocalDateTime.of(2024, 3, 28, 8, 30)
                    )
                }
            }
        }

        context("잔류 신청하는 날이 아니면") {
            it("잔류 신청이 불가능하다") {
                shouldThrow<RemainCanNotAppliedException> {
                    remainAvailableTime.checkAvailable(
                        LocalDateTime.of(2024, 3, 28, 8, 29)
                    )
                }
            }
        }

        context("잔류 신청 시간 이전에 외출 신청하면") {
            it("잔류 신청이 불가능하다") {
                shouldThrow<RemainCanNotAppliedException> {
                    remainAvailableTime.checkAvailable(
                        LocalDateTime.of(2024, 3, 28, 8, 29)
                    )
                }
            }
        }

        context("잔류 신청 시간 이후에 외출 신청하면") {
            it("잔류 신청이 불가능하다") {
                shouldThrow<RemainCanNotAppliedException> {
                    remainAvailableTime.checkAvailable(
                        LocalDateTime.of(2024, 3, 28, 10, 1)
                    )
                }
            }
        }
    }
})
