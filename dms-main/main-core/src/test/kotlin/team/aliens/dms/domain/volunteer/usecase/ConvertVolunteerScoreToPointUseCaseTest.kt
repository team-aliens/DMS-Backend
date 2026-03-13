package team.aliens.dms.domain.volunteer.usecase

import io.kotest.assertions.throwables.shouldNotThrowAny
import io.kotest.core.spec.style.DescribeSpec
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import io.mockk.verify
import team.aliens.dms.domain.point.service.PointService
import team.aliens.dms.domain.volunteer.service.VolunteerService
import team.aliens.dms.domain.volunteer.spi.vo.VolunteerScoreWithStudentVO
import java.util.UUID

class ConvertVolunteerScoreToPointUseCaseTest : DescribeSpec({

    describe("execute") {
        context("봉사 점수가 존재하는 경우") {
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

            every { volunteerService.getAllVolunteerScoresWithVO() } returns volunteerScores
            every { volunteerService.deleteAllVolunteerScores() } just runs
            coEvery { pointService.saveAllPointHistories(any(), any()) } returns emptyList()

            it("봉사 점수를 상점 이력으로 변환한다") {
                shouldNotThrowAny {
                    useCase.execute()
                }

                coVerify(exactly = 1) {
                    pointService.saveAllPointHistories(any(), listOf(studentId1, studentId2))
                }
                verify(exactly = 1) { volunteerService.getAllVolunteerScoresWithVO() }
                verify(exactly = 1) { volunteerService.deleteAllVolunteerScores() }
            }
        }

        context("봉사 점수가 없는 경우") {
            val volunteerService = mockk<VolunteerService>()
            val pointService = mockk<PointService>()
            val useCase = ConvertVolunteerScoreToPointUseCase(volunteerService, pointService)

            every { volunteerService.getAllVolunteerScoresWithVO() } returns emptyList()
            every { volunteerService.deleteAllVolunteerScores() } just runs
            coEvery { pointService.saveAllPointHistories(any(), any()) } returns emptyList()

            it("봉사 점수가 없어도 정상 처리한다") {
                shouldNotThrowAny {
                    useCase.execute()
                }

                coVerify(exactly = 1) { pointService.saveAllPointHistories(emptyList(), emptyList()) }
                verify(exactly = 1) { volunteerService.getAllVolunteerScoresWithVO() }
                verify(exactly = 1) { volunteerService.deleteAllVolunteerScores() }
            }
        }
    }
})