package team.aliens.dms.domain.daybreak.usecase

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.DescribeSpec
import io.mockk.clearMocks
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import io.mockk.verify
import team.aliens.dms.domain.daybreak.exception.DaybreakStudyApplicationNotFoundException
import team.aliens.dms.domain.daybreak.exception.DaybreakStudyApplicationCanNotCancelException
import team.aliens.dms.domain.daybreak.model.Status
import team.aliens.dms.domain.daybreak.service.DaybreakService
import team.aliens.dms.domain.daybreak.spi.vo.DaybreakStudyApplicationStatusVO
import team.aliens.dms.domain.student.model.Student
import team.aliens.dms.domain.student.service.StudentService
import java.time.LocalDate
import java.util.UUID

class CancelDaybreakStudyApplicationUseCaseTest : DescribeSpec({

    val daybreakService = mockk<DaybreakService>()
    val studentService = mockk<StudentService>()

    val useCase = CancelDaybreakStudyApplicationUseCase(daybreakService, studentService)

    beforeTest {
        clearMocks(daybreakService, studentService)
    }

    describe("execute") {

        val studentId = UUID.randomUUID()

        val mockStudent = mockk<Student> {
            every { id } returns studentId
        }

        context("기존 PENDING 상태인 신청이 있을 때 취소 요청이 들어오면") {

            val applicationStatus = DaybreakStudyApplicationStatusVO(
                status = Status.PENDING,
                startDate = LocalDate.now(),
                endDate = LocalDate.now().plusDays(3),
            )

            it("신청을 삭제한다") {
                every { studentService.getCurrentStudent() } returns mockStudent
                every { daybreakService.getRecentDaybreakStudyApplicationStatusByStudentId(studentId) } returns applicationStatus
                every { daybreakService.deleteDaybreakStudyApplication(studentId) } just runs
                useCase.execute()

                verify(exactly = 1) { daybreakService.deleteDaybreakStudyApplication(studentId) }
            }
        }

        context("PENDING 상태가 아닌 신청일 때 요청이 들어오면") {

            val applicationStatus = DaybreakStudyApplicationStatusVO(
                Status.FIRST_APPROVED,
                startDate = LocalDate.now(),
                endDate = LocalDate.now().plusDays(3)
            )

            it("예외를 반환한다.") {
                every { studentService.getCurrentStudent() } returns mockStudent
                every { daybreakService.getRecentDaybreakStudyApplicationStatusByStudentId(studentId) } returns applicationStatus
                every { daybreakService.deleteDaybreakStudyApplication(studentId) } just runs

                shouldThrow<DaybreakStudyApplicationCanNotCancelException> {
                    useCase.execute()
                }

                verify(exactly = 0) { daybreakService.deleteDaybreakStudyApplication(studentId) }

            }
        }

        context("신청이 없을 때 요청이 들어오면") {
            it("예외를 반환한다.") {
                every { studentService.getCurrentStudent() } returns mockStudent
                every { daybreakService.getRecentDaybreakStudyApplicationStatusByStudentId(studentId) } throws DaybreakStudyApplicationNotFoundException
                every { daybreakService.deleteDaybreakStudyApplication(studentId) } just runs

                shouldThrow<DaybreakStudyApplicationNotFoundException> {
                    useCase.execute()
                }

                verify(exactly = 0) { daybreakService.deleteDaybreakStudyApplication(studentId) }

            }
        }
    }
})
