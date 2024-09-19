package team.aliens.dms.domain.outing.model

import io.kotest.assertions.throwables.shouldNotThrowAny
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.DescribeSpec
import team.aliens.dms.domain.outing.exception.OutingAvailableTimeMismatchException
import team.aliens.dms.domain.outing.stub.createOutingAvailableTimeStub
import java.time.DayOfWeek
import java.time.LocalTime

class OutingAvailableTimeTest : DescribeSpec({

    describe("checkAvailable") {
        val outingAvailableTime = createOutingAvailableTimeStub(
            dayOfWeek = DayOfWeek.SUNDAY,
            outingTime = LocalTime.of(12, 0, 0),
            arrivalTime = LocalTime.of(20, 30, 0)
        )

        context("외출 신청한 시간이 외출 가능 시간이면") {
            it("외출 신청이 가능하다") {
                shouldNotThrowAny {
                    outingAvailableTime.checkAvailable(
                        outingTime = LocalTime.of(12, 0, 0),
                        arrivalTime = LocalTime.of(20, 30, 0)
                    )
                }
            }
        }

        context("외출 가능 시간 전에 외출 신청을 하면") {
            it("예외가 발생한다") {
                shouldThrow<OutingAvailableTimeMismatchException> {
                    outingAvailableTime.checkAvailable(
                        outingTime = LocalTime.of(11, 59, 59),
                        arrivalTime = LocalTime.of(20, 0, 0)
                    )
                }
            }
        }

        context("외출 도착 시간 후에 외출 신청을 하면") {
            it("예외가 발생한다") {
                shouldThrow<OutingAvailableTimeMismatchException> {
                    outingAvailableTime.checkAvailable(
                        outingTime = LocalTime.of(14, 0, 0),
                        arrivalTime = LocalTime.of(20, 30, 1)
                    )
                }
            }
        }
    }
})
