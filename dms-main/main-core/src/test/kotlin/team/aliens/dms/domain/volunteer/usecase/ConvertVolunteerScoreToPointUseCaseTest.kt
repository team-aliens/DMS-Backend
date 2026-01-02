package team.aliens.dms.domain.volunteer.usecase

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import team.aliens.dms.domain.point.model.PointHistory
import team.aliens.dms.domain.point.model.PointOption
import team.aliens.dms.domain.point.model.PointType
import team.aliens.dms.domain.point.service.CommandPointService
import team.aliens.dms.domain.point.service.PointService
import team.aliens.dms.domain.point.stub.GetPointServiceStub
import team.aliens.dms.domain.student.model.Student
import team.aliens.dms.domain.volunteer.model.Volunteer
import team.aliens.dms.domain.volunteer.model.VolunteerApplication
import team.aliens.dms.domain.volunteer.model.VolunteerApplicationStatus
import team.aliens.dms.domain.volunteer.service.CommandVolunteerService
import team.aliens.dms.domain.volunteer.service.GetVolunteerService
import team.aliens.dms.domain.volunteer.service.VolunteerService
import team.aliens.dms.domain.volunteer.spi.vo.CurrentVolunteerApplicantVO
import team.aliens.dms.domain.volunteer.spi.vo.VolunteerApplicantVO
import team.aliens.dms.domain.volunteer.spi.vo.VolunteerScoreWithStudentVO
import team.aliens.dms.domain.volunteer.spi.vo.VolunteerWithCurrentApplicantVO
import team.aliens.dms.domain.volunteer.stub.GetVolunteerServiceStub
import java.util.UUID

class ConvertVolunteerScoreToPointUseCaseTest : DescribeSpec({

    class FakeGetVolunteerService : GetVolunteerServiceStub() {
        var volunteerScores: List<VolunteerScoreWithStudentVO> = emptyList()
        override fun getAllVolunteerScoresWithVO(): List<VolunteerScoreWithStudentVO> = volunteerScores
    }

    class FakeCommandVolunteerService : CommandVolunteerServiceStub() {
        var isScoresDeleted = false
        override fun deleteAllVolunteerScores() {
            isScoresDeleted = true
        }
    }

    class FakeCommandPointService : CommandPointServiceStub() {
        var savedPointHistories: List<PointHistory> = emptyList()
        var savedStudentIds: List<UUID> = emptyList()

        override fun saveAllPointHistories(pointHistories: List<PointHistory>, studentIds: List<UUID>?) {
            savedPointHistories = pointHistories
            savedStudentIds = studentIds ?: emptyList()
        }
    }

    class FakeVolunteerService(
        private val getService: FakeGetVolunteerService,
        private val commandService: FakeCommandVolunteerService
    ) : VolunteerService(
        checkVolunteerService = object : CheckVolunteerServiceStub() {},
        commandVolunteerService = commandService,
        getVolunteerService = getService
    )

    class FakePointService(
        private val commandService: FakeCommandPointService
    ) : PointService(
        getPointService = object : GetPointServiceStub() {},
        commandPointService = commandService
    )

    describe("execute") {
        context("봉사 점수가 있을 때") {
            val fakeGetVolunteerService = FakeGetVolunteerService()
            val fakeCommandVolunteerService = FakeCommandVolunteerService()
            val fakeCommandPointService = FakeCommandPointService()

            val volunteerService = FakeVolunteerService(fakeGetVolunteerService, fakeCommandVolunteerService)
            val pointService = FakePointService(fakeCommandPointService)
            val useCase = ConvertVolunteerScoreToPointUseCase(volunteerService, pointService)

            val schoolId = UUID.randomUUID()
            val studentId1 = UUID.randomUUID()
            val studentId2 = UUID.randomUUID()

            fakeGetVolunteerService.volunteerScores = listOf(
                VolunteerScoreWithStudentVO(
                    studentId = studentId1,
                    studentName = "홍길동",
                    studentGrade = 1,
                    studentClassRoom = 1,
                    studentNumber = 1,
                    assignScore = 10,
                    bonusTotal = 5,
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

            useCase.execute()

            it("각 학생에게 올바른 상점이 부여된다") {
                fakeCommandPointService.savedPointHistories shouldHaveSize 2

                val firstHistory = fakeCommandPointService.savedPointHistories[0]
                firstHistory.studentName shouldBe "홍길동"
                firstHistory.studentGcn shouldBe "1101"
                firstHistory.bonusTotal shouldBe 15
                firstHistory.minusTotal shouldBe 0
                firstHistory.pointScore shouldBe 10
                firstHistory.pointName shouldBe "봉사활동 상점"
                firstHistory.pointType shouldBe PointType.BONUS
                firstHistory.isCancel shouldBe false
                firstHistory.schoolId shouldBe schoolId

                val secondHistory = fakeCommandPointService.savedPointHistories[1]
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
                fakeCommandPointService.savedStudentIds shouldBe listOf(studentId1, studentId2)
            }

            it("봉사 점수가 삭제된다") {
                fakeCommandVolunteerService.isScoresDeleted shouldBe true
            }
        }

        context("봉사 점수가 없을 때") {
            val fakeGetVolunteerService = FakeGetVolunteerService()
            val fakeCommandVolunteerService = FakeCommandVolunteerService()
            val fakeCommandPointService = FakeCommandPointService()

            val volunteerService = FakeVolunteerService(fakeGetVolunteerService, fakeCommandVolunteerService)
            val pointService = FakePointService(fakeCommandPointService)
            val useCase = ConvertVolunteerScoreToPointUseCase(volunteerService, pointService)

            useCase.execute()

            it("상점이 부여되지 않는다") {
                fakeCommandPointService.savedPointHistories.shouldBeEmpty()
                fakeCommandPointService.savedStudentIds.shouldBeEmpty()
            }

            it("봉사 점수 삭제는 수행된다") {
                fakeCommandVolunteerService.isScoresDeleted shouldBe true
            }
        }
    }
})
