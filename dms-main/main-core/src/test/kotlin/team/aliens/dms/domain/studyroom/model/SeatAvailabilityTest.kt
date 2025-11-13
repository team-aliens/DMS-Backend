package team.aliens.dms.domain.studyroom.model

import io.kotest.assertions.throwables.shouldNotThrowAny
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.DescribeSpec
import team.aliens.dms.domain.studyroom.exception.SeatCanNotAppliedException
import team.aliens.dms.domain.studyroom.stub.createSeatStub

class SeatAvailabilityTest : DescribeSpec({

    describe("checkAvailable") {
        context("좌석 상태가 AVAILABLE이면") {
            val seat = createSeatStub(status = SeatStatus.AVAILABLE)

            it("예외가 발생하지 않는다") {
                shouldNotThrowAny {
                    seat.checkAvailable()
                }
            }
        }

        context("좌석 상태가 UNAVAILABLE이면") {
            val seat = createSeatStub(status = SeatStatus.UNAVAILABLE)

            it("SeatCanNotAppliedException이 발생한다") {
                shouldThrow<SeatCanNotAppliedException> {
                    seat.checkAvailable()
                }
            }
        }

        context("좌석 상태가 EMPTY이면") {
            val seat = createSeatStub(status = SeatStatus.EMPTY)

            it("SeatCanNotAppliedException이 발생한다") {
                shouldThrow<SeatCanNotAppliedException> {
                    seat.checkAvailable()
                }
            }
        }
    }
})
