package team.aliens.dms.domain.studyroom.model

import io.kotest.assertions.throwables.shouldNotThrowAny
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.DescribeSpec
import team.aliens.dms.domain.studyroom.exception.SeatCanNotAppliedException
import team.aliens.dms.domain.studyroom.stub.createSeatStub

class SeatTest : DescribeSpec({

    describe("checkAvailable") {
        context("자리 상태가 AVAILABLE이면") {
            val seat = createSeatStub(status = SeatStatus.AVAILABLE)

            it("자리를 이용할 수 있다") {
                shouldNotThrowAny {
                    seat.checkAvailable()
                }
            }
        }

        context("자리 상태가 UNAVAILABLE이면") {
            val seat = createSeatStub(status = SeatStatus.UNAVAILABLE)

            it("자리를 이용할 수 없다") {
                shouldThrow<SeatCanNotAppliedException> {
                    seat.checkAvailable()
                }
            }
        }

        context("자리 상태가 EMPTY이면") {
            val seat = createSeatStub(status = SeatStatus.EMPTY)

            it("자리를 이용할 수 없다") {
                shouldThrow<SeatCanNotAppliedException> {
                    seat.checkAvailable()
                }
            }
        }
    }
})
