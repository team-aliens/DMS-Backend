package team.aliens.dms.domain.daybreak.usecase

import io.kotest.assertions.throwables.shouldNotThrowAny
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldNotBe
import io.mockk.every
import io.mockk.mockk
import team.aliens.dms.common.dto.PageData
import team.aliens.dms.common.service.security.SecurityService
import team.aliens.dms.domain.daybreak.model.Status
import team.aliens.dms.domain.daybreak.service.DaybreakService
import team.aliens.dms.domain.teacher.model.Teacher
import team.aliens.dms.domain.teacher.service.TeacherService
import java.time.LocalDate
import java.util.UUID

class QueryHeadTeacherDaybreakStudyApplicationUseCaseTest : DescribeSpec({

    val daybreakService = mockk<DaybreakService>()
    val securityService = mockk<SecurityService>()
    val teacherService = mockk<TeacherService>()

    val useCase = QueryHeadTeacherDaybreakStudyApplicationUseCase(daybreakService, securityService, teacherService)

    describe("execute"){
        context("부장 선생님이 새벽 자습 신청 목록을 조회하면"){

            val userId = UUID.randomUUID()
            val typeId = UUID.randomUUID()
            val date = LocalDate.now()
            val status = Status.FIRST_APPROVED
            val pageData = PageData(page = 1, size = 10)
            val grade = 1


            val mockTeacher = mockk<Teacher> {
                every { this@mockk.grade } returns grade
            }

            every { securityService.getCurrentUserId() } returns userId
            every { teacherService.getTeacherById(userId) } returns mockTeacher
            every { daybreakService.headTeacherGetDaybreakStudyApplications(grade, typeId, date, status, pageData) } returns mockk()

            it("새벽 자습 신청 목록을 반환한다"){
                shouldNotThrowAny {
                    val response = useCase.execute(
                        typeId = typeId,
                        date = date,
                        status = status,
                        pageData = pageData
                    )

                    response shouldNotBe null
                }
            }
        }
    }

})
