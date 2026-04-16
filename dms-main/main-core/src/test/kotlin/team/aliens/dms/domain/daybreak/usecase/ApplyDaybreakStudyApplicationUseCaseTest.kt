package team.aliens.dms.domain.daybreak.usecase

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.DescribeSpec
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import io.mockk.verify
import team.aliens.dms.common.service.security.SecurityService
import team.aliens.dms.domain.daybreak.dto.request.ApplyDaybreakStudyApplicationRequest
import team.aliens.dms.domain.daybreak.exception.DaybreakStudyApplicationAlreadyExistsException
import team.aliens.dms.domain.daybreak.model.DaybreakStudyType
import team.aliens.dms.domain.daybreak.service.DaybreakService
import team.aliens.dms.domain.student.model.Student
import team.aliens.dms.domain.student.service.StudentService
import java.time.LocalDate
import java.util.UUID

class ApplyDaybreakStudyApplicationUseCaseTest : DescribeSpec({

    val daybreakService = mockk<DaybreakService>()
    val securityService = mockk<SecurityService>()
    val studentService = mockk<StudentService>()

    val useCase = ApplyDaybreakStudyApplicationUseCase(
        daybreakService,
        securityService,
        studentService
    )

    describe("execute") {
        context("올바른 신청 요청이 들어오면") {
            val studentId = UUID.randomUUID()
            val schoolId = UUID.randomUUID()
            val typeId = UUID.randomUUID()

            val request = ApplyDaybreakStudyApplicationRequest(
                typeId = typeId,
                startDate = LocalDate.now(),
                endDate = LocalDate.now().plusDays(3),
                reason = "아침 공부",
                teacherId = UUID.randomUUID()
            )

            val mockStudent = mockk<Student> {
                every { id } returns studentId
            }
            val mockStudyType = mockk<DaybreakStudyType> {
                every { id } returns typeId
            }

            it("신청 정보를 정상적으로 저장한다") {

                // given
                every { studentService.getCurrentStudent() } returns mockStudent
                every { securityService.getCurrentSchoolId() } returns schoolId
                every { daybreakService.checkDaybreakStudyApplicationExists(studentId) } just runs
                every { daybreakService.getDaybreakStudyTypeById(typeId) } returns mockStudyType
                every { daybreakService.saveDaybreakStudyApplication(any()) } just runs

                // when
                useCase.execute(request)

                verify(exactly = 1) {
                    daybreakService.checkDaybreakStudyApplicationExists(studentId)
                }
                verify(exactly = 1) {
                    daybreakService.saveDaybreakStudyApplication(any())
                }


            }
        }
        context("이미 신청내역이 존재한다면") {
            val studentId = UUID.randomUUID()
            val mockStudent = mockk<Student> {
                every { id } returns studentId
            }

            it("예외가 발생한다") {

                // given
                every { studentService.getCurrentStudent() } returns mockStudent
                every { securityService.getCurrentSchoolId() } returns UUID.randomUUID()
                every { daybreakService.checkDaybreakStudyApplicationExists(studentId) } throws DaybreakStudyApplicationAlreadyExistsException

                // when & then
                shouldThrow<DaybreakStudyApplicationAlreadyExistsException> {
                    useCase.execute(mockk(relaxed = true))
                }
            }
        }
    }
})