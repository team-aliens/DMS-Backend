package team.aliens.dms.domain.teahcer.usecase

import io.kotest.assertions.throwables.shouldNotThrowAny
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldNotBe
import io.mockk.every
import io.mockk.mockk
import team.aliens.dms.common.service.security.SecurityService
import team.aliens.dms.domain.teacher.service.TeacherService
import team.aliens.dms.domain.teacher.usecase.QueryGeneralTeacherUseCase
import java.util.UUID

class QueryGeneralTeacherUseCaseTest : DescribeSpec({

    val teacherService = mockk<TeacherService>()
    val securityService = mockk<SecurityService>()

    val useCase = QueryGeneralTeacherUseCase(teacherService, securityService)

    describe("execute") {
        context("학생이 담당선생님을 조회하면"){

            val schoolId = UUID.randomUUID()

            every { securityService.getCurrentSchoolId() } returns schoolId
            every { teacherService.getGeneralTeachersBySchoolId(schoolId) } returns emptyList()

            it("담당선생님을 반환한다"){
                shouldNotThrowAny{
                    val response = useCase.execute()

                    response shouldNotBe null
                }
            }
        }
    }
})
