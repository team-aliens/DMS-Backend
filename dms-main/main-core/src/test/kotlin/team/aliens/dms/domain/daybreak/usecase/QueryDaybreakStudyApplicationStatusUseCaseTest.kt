package team.aliens.dms.domain.daybreak.usecase

import io.kotest.assertions.throwables.shouldNotThrowAny
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.mockk.every
import io.mockk.mockk
import team.aliens.dms.domain.daybreak.service.DaybreakService
import team.aliens.dms.domain.student.model.Student
import team.aliens.dms.domain.student.service.StudentService
import java.util.UUID

class QueryDaybreakStudyApplicationStatusUseCaseTest : DescribeSpec({

    val daybreakService = mockk<DaybreakService>()
    val studentService = mockk<StudentService>()

    val useCase = QueryDaybreakStudyApplicationStatusUseCase(daybreakService, studentService)

    describe("execute"){
        context("학생이 자신의 새벽 자습 신청 상태를 조회하면") {
            val student = mockk<Student>()

            every { studentService.getCurrentStudent() } returns student
            every { daybreakService.getRecentDaybreakStudyApplicationStatusByStudentId(student.id) } returns mockk(relaxed = true)

            it("자신의 새벽 자습 신청 상태를 반환한다"){
                shouldNotThrowAny {
                    val response = useCase.execute()

                    response shouldNotBe null
                }
            }

        }

    }

})
