package team.aliens.dms.domain.studyroom.model

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import java.time.LocalTime
import java.util.UUID

class TimeSlotTest : DescribeSpec({

    describe("name") {
        context("시작 시간과 종료 시간이 주어지면") {
            it("HH:mm 형식으로 포맷팅된 타임슬롯 이름을 생성한다") {
                val schoolId = UUID.randomUUID()
                val startTime = LocalTime.of(9, 0)
                val endTime = LocalTime.of(10, 30)

                val timeSlot = TimeSlot(
                    schoolId = schoolId,
                    startTime = startTime,
                    endTime = endTime
                )

                timeSlot.name shouldBe "09:00 ~ 10:30"
            }
        }

        context("정오와 자정 시간이 주어지면") {
            it("올바른 형식으로 타임슬롯 이름을 생성한다") {
                val schoolId = UUID.randomUUID()
                val startTime = LocalTime.MIDNIGHT
                val endTime = LocalTime.NOON

                val timeSlot = TimeSlot(
                    schoolId = schoolId,
                    startTime = startTime,
                    endTime = endTime
                )

                timeSlot.name shouldBe "00:00 ~ 12:00"
            }
        }

        context("오후 시간대가 주어지면") {
            it("24시간 형식으로 타임슬롯 이름을 생성한다") {
                val schoolId = UUID.randomUUID()
                val startTime = LocalTime.of(14, 15)
                val endTime = LocalTime.of(16, 45)

                val timeSlot = TimeSlot(
                    schoolId = schoolId,
                    startTime = startTime,
                    endTime = endTime
                )

                timeSlot.name shouldBe "14:15 ~ 16:45"
            }
        }

        context("늦은 밤 시간대가 주어지면") {
            it("올바른 형식으로 타임슬롯 이름을 생성한다") {
                val schoolId = UUID.randomUUID()
                val startTime = LocalTime.of(22, 30)
                val endTime = LocalTime.of(23, 59)

                val timeSlot = TimeSlot(
                    schoolId = schoolId,
                    startTime = startTime,
                    endTime = endTime
                )

                timeSlot.name shouldBe "22:30 ~ 23:59"
            }
        }
    }
})
