package team.aliens.dms.domain.volunteer.usecase

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import io.mockk.slot
import io.mockk.verify
import team.aliens.dms.domain.point.model.PointHistory
import team.aliens.dms.domain.point.model.PointType
import team.aliens.dms.domain.point.service.PointService
import team.aliens.dms.domain.volunteer.service.VolunteerService
import team.aliens.dms.domain.volunteer.spi.vo.VolunteerScoreWithStudentVO
import java.util.UUID

class ConvertVolunteerScoreToPointUseCaseTest : DescribeSpec({

    describe("execute") {
        context("봉사 점수가 있을 때") {
            val volunteerService = mockk<VolunteerService>()
            val pointService = mockk<PointService>()
            val useCase = ConvertVolunteerScoreToPointUseCase(volunteerService, pointService)

            val schoolId = UUID.randomUUID()
            val studentId1 = UUID.randomUUID()
            val studentId2 = UUID.randomUUID()

            val volunteerScores = listOf(
                VolunteerScoreWithStudentVO(
                    studentId = studentId1,
                    studentName = "홍길동",
                    studentGrade = 1,
                    studentClassRoom = 1,
                    studentNumber = 1,
                    assignScore = 10,
                    bonusTotal = 5,
                    minusTotal = 0,
                    schoolId = schoolId
                ),
                VolunteerScoreWithStudentVO(
                    studentId = studentId2,
                    studentName = "김철수",
                    studentGrade = 2,
                    studentClassRoom = 2,
                    studentNumber = 5,
                    assignScore = 15,
                    bonusTotal = 10,
                    minusTotal = 3,
                    schoolId = schoolId
                )
            )

            val pointHistoriesSlot = slot<List<PointHistory>>()

            every { volunteerService.getAllVolunteerScoresWithVO() } returns volunteerScores
            every { volunteerService.deleteAllVolunteerScores() } just runs
            every { pointService.saveAllPointHistories(capture(pointHistoriesSlot), any()) } just runs

            useCase.execute()

            it("각 학생에게 올바른 상점이 부여된다") {
                val savedPointHistories = pointHistoriesSlot.captured
                savedPointHistories shouldHaveSize 2

                val firstHistory = savedPointHistories[0]
                firstHistory.studentName shouldBe "홍길동"
                firstHistory.studentGcn shouldBe "1101"
                firstHistory.bonusTotal shouldBe 15
                firstHistory.minusTotal shouldBe 0
                firstHistory.pointScore shouldBe 10
                firstHistory.pointName shouldBe "봉사활동 상점"
                firstHistory.pointType shouldBe PointType.BONUS
                firstHistory.isCancel shouldBe false
                firstHistory.schoolId shouldBe schoolId

                val secondHistory = savedPointHistories[1]
                secondHistory.studentName shouldBe "김철수"
                secondHistory.studentGcn shouldBe "2205"
                secondHistory.bonusTotal shouldBe 25
                secondHistory.minusTotal shouldBe 3
                secondHistory.pointScore shouldBe 15
                secondHistory.pointName shouldBe "봉사활동 상점"
                secondHistory.pointType shouldBe PointType.BONUS
                secondHistory.isCancel shouldBe false
                secondHistory.schoolId shouldBe schoolId
            }

            it("학생 ID가 올바르게 저장된다") {
                verify { pointService.saveAllPointHistories(any(), listOf(studentId1, studentId2)) }
            }

            it("봉사 점수가 삭제된다") {
                verify(exactly = 1) { volunteerService.deleteAllVolunteerScores() }
            }
        }

        context("봉사 점수가 없을 때") {
            val volunteerService = mockk<VolunteerService>()
            val pointService = mockk<PointService>(relaxed = true)
            val useCase = ConvertVolunteerScoreToPointUseCase(volunteerService, pointService)

            every { volunteerService.getAllVolunteerScoresWithVO() } returns emptyList()
            every { volunteerService.deleteAllVolunteerScores() } just runs

            useCase.execute()

            it("상점이 부여되지 않는다") {
                verify(exactly = 1) { pointService.saveAllPointHistories(emptyList(), emptyList()) }
            }

            it("봉사 점수 삭제는 수행된다") {
                verify(exactly = 1) { volunteerService.deleteAllVolunteerScores() }
            }
        }
    }
})
