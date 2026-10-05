package team.aliens.dms.domain.remain.model

import io.kotest.assertions.throwables.shouldNotThrowAny
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.data.forAll
import io.kotest.data.row
import team.aliens.dms.domain.remain.exception.RemainCanNotAppliedException
import team.aliens.dms.domain.remain.stub.createRemainAvailableTimeStub
import java.time.DayOfWeek
import java.time.LocalDateTime
import java.time.LocalTime

class RemainAvailableTimeTest : DescribeSpec({

    describe("checkAvailable") {

        context("신청 가능 시간이 하루 안에 있을 때") {
            // 목요일 08:30 ~ 10:00
            val remainAvailableTime = createRemainAvailableTimeStub(
                startDayOfWeek = DayOfWeek.THURSDAY,
                startTime = LocalTime.of(8, 30, 0),
                endDayOfWeek = DayOfWeek.THURSDAY,
                endTime = LocalTime.of(10, 0, 0)
            )

            it("시작·종료 시각을 포함해 그 사이면 신청할 수 있다") {
                forAll(
                    row(LocalDateTime.of(2024, 3, 28, 8, 30)),
                    row(LocalDateTime.of(2024, 3, 28, 9, 15)),
                    row(LocalDateTime.of(2024, 3, 28, 10, 0))
                ) { now ->
                    shouldNotThrowAny { remainAvailableTime.checkAvailable(now) }
                }
            }

            it("시작 시각 전이면 신청할 수 없다") {
                shouldThrow<RemainCanNotAppliedException> {
                    remainAvailableTime.checkAvailable(LocalDateTime.of(2024, 3, 28, 8, 29))
                }
            }

            it("종료 시각 후면 신청할 수 없다") {
                shouldThrow<RemainCanNotAppliedException> {
                    remainAvailableTime.checkAvailable(LocalDateTime.of(2024, 3, 28, 10, 1))
                }
            }

            it("다른 요일이면 시각이 범위 안이어도 신청할 수 없다") {
                forAll(
                    row(LocalDateTime.of(2024, 3, 27, 9, 0)), // 수요일
                    row(LocalDateTime.of(2024, 3, 29, 9, 0)) // 금요일
                ) { now ->
                    shouldThrow<RemainCanNotAppliedException> { remainAvailableTime.checkAvailable(now) }
                }
            }
        }

        context("신청 가능 시간이 여러 날에 걸쳐 있을 때") {
            // 화요일 09:00 ~ 목요일 18:00
            val remainAvailableTime = createRemainAvailableTimeStub(
                startDayOfWeek = DayOfWeek.TUESDAY,
                startTime = LocalTime.of(9, 0),
                endDayOfWeek = DayOfWeek.THURSDAY,
                endTime = LocalTime.of(18, 0)
            )

            it("시작일은 시작 시각부터, 종료일은 종료 시각까지 신청할 수 있다") {
                forAll(
                    row(LocalDateTime.of(2024, 3, 26, 9, 0)), // 화요일 시작 시각
                    row(LocalDateTime.of(2024, 3, 26, 23, 59)), // 화요일 늦은 시각
                    row(LocalDateTime.of(2024, 3, 28, 0, 0)), // 목요일 이른 시각
                    row(LocalDateTime.of(2024, 3, 28, 18, 0)) // 목요일 종료 시각
                ) { now ->
                    shouldNotThrowAny { remainAvailableTime.checkAvailable(now) }
                }
            }

            it("중간 요일은 시각과 관계없이 신청할 수 있다") {
                forAll(
                    row(LocalDateTime.of(2024, 3, 27, 0, 0)), // 수요일
                    row(LocalDateTime.of(2024, 3, 27, 23, 59))
                ) { now ->
                    shouldNotThrowAny { remainAvailableTime.checkAvailable(now) }
                }
            }

            it("시작일의 시작 시각 전이면 신청할 수 없다") {
                shouldThrow<RemainCanNotAppliedException> {
                    remainAvailableTime.checkAvailable(LocalDateTime.of(2024, 3, 26, 8, 59))
                }
            }

            it("종료일의 종료 시각 후면 신청할 수 없다") {
                shouldThrow<RemainCanNotAppliedException> {
                    remainAvailableTime.checkAvailable(LocalDateTime.of(2024, 3, 28, 18, 1))
                }
            }

            it("시작 요일 전이나 종료 요일 후면 신청할 수 없다") {
                forAll(
                    row(LocalDateTime.of(2024, 3, 25, 12, 0)), // 월요일
                    row(LocalDateTime.of(2024, 3, 29, 12, 0)) // 금요일
                ) { now ->
                    shouldThrow<RemainCanNotAppliedException> { remainAvailableTime.checkAvailable(now) }
                }
            }
        }
    }
})
