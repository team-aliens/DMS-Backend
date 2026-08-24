package team.aliens.dms.domain.daybreak.usecase

import io.kotest.assertions.throwables.shouldNotThrowAny
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import team.aliens.dms.common.service.security.SecurityService
import team.aliens.dms.domain.daybreak.exception.DaybreakStudentSchoolMismatchException
import team.aliens.dms.domain.daybreak.service.DaybreakService
import team.aliens.dms.domain.daybreak.spi.vo.DaybreakStudyApplicationVO
import team.aliens.dms.domain.student.model.Student
import team.aliens.dms.domain.student.service.StudentService
import java.util.UUID

class QueryStudentDaybreakStudyApplicationHistoryUseCaseTest : DescribeSpec({

    describe("execute") {
        context("같은 학교 소속 학생의 이력을 조회하면") {
            it("이력 목록을 반환한다") {

                // given
                val daybreakService = mockk<DaybreakService>()
                val studentService = mockk<StudentService>()
                val securityService = mockk<SecurityService>()
                val useCase = QueryStudentDaybreakStudyApplicationHistoryUseCase(
                    daybreakService,
                    studentService,
                    securityService
                )

                val studentId = UUID.randomUUID()
                val schoolId = UUID.randomUUID()

                val mockStudent = mockk<Student> {
                    every { this@mockk.schoolId } returns schoolId
                }
                val mockApplications = listOf(mockk<DaybreakStudyApplicationVO>())

                every { studentService.getStudentById(studentId) } returns mockStudent
                every { securityService.getCurrentSchoolId() } returns schoolId
                every {
                    daybreakService.getStudentDaybreakStudyApplicationHistoryByStudentId(studentId)
                } returns mockApplications

                // when & then
                shouldNotThrowAny {
                    val response = useCase.execute(studentId)

                    response.applications shouldBe mockApplications
                }

                verify(exactly = 1) {
                    daybreakService.getStudentDaybreakStudyApplicationHistoryByStudentId(studentId)
                }
            }
        }

        context("다른 학교 소속 학생의 이력을 조회하면") {
            it("DaybreakStudentSchoolMismatchException이 발생한다") {

                // given
                val daybreakService = mockk<DaybreakService>()
                val studentService = mockk<StudentService>()
                val securityService = mockk<SecurityService>()
                val useCase = QueryStudentDaybreakStudyApplicationHistoryUseCase(
                    daybreakService,
                    studentService,
                    securityService
                )

                val studentId = UUID.randomUUID()

                val mockStudent = mockk<Student> {
                    every { this@mockk.schoolId } returns UUID.randomUUID()
                }

                every { studentService.getStudentById(studentId) } returns mockStudent
                every { securityService.getCurrentSchoolId() } returns UUID.randomUUID()

                // when & then
                shouldThrow<DaybreakStudentSchoolMismatchException> {
                    useCase.execute(studentId)
                }

                verify(exactly = 0) {
                    daybreakService.getStudentDaybreakStudyApplicationHistoryByStudentId(any())
                }
            }
        }
    }
})
