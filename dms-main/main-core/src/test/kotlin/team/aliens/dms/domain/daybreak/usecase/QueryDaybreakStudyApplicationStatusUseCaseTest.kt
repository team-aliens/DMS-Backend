package team.aliens.dms.domain.daybreak.usecase

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import team.aliens.dms.domain.daybreak.model.Status
import team.aliens.dms.domain.daybreak.service.DaybreakService
import team.aliens.dms.domain.daybreak.spi.vo.DaybreakStudyApplicationStatusVO
import team.aliens.dms.domain.student.service.StudentService
import team.aliens.dms.domain.student.stub.createStudentStub
import java.time.LocalDate

class QueryDaybreakStudyApplicationStatusUseCaseTest : DescribeSpec({

    val daybreakService = mockk<DaybreakService>()
    val studentService = mockk<StudentService>()

    val useCase = QueryDaybreakStudyApplicationStatusUseCase(daybreakService, studentService)

    describe("execute") {
        context("학생이 자신의 새벽 자습 신청 상태를 조회하면") {
            val student = createStudentStub()

            val statusInfo = DaybreakStudyApplicationStatusVO(
                status = Status.FIRST_APPROVED,
                dates = listOf(LocalDate.of(2026, 10, 26), LocalDate.of(2026, 10, 28)),
                typeName = "자습 유형",
                reason = "신청 사유",
                teacherName = "담당 선생님"
            )

            every { studentService.getCurrentStudent() } returns student
            every { daybreakService.getRecentDaybreakStudyApplicationStatusByStudentId(student.id) } returns statusInfo

            it("가장 최근 신청의 상태·날짜·유형·사유·담당 선생님 이름을 반환한다") {
                val response = useCase.execute()

                response.status shouldBe Status.FIRST_APPROVED
                response.dates shouldBe listOf(LocalDate.of(2026, 10, 26), LocalDate.of(2026, 10, 28))
                response.typeName shouldBe "자습 유형"
                response.reason shouldBe "신청 사유"
                response.teacherName shouldBe "담당 선생님"
            }
        }
    }
})
