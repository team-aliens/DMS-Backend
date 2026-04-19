package team.aliens.dms.domain.daybreak.usecase

import io.kotest.assertions.throwables.shouldNotThrowAny
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldNotBe
import io.mockk.every
import io.mockk.mockk
import team.aliens.dms.common.dto.PageData
import team.aliens.dms.common.service.security.SecurityService
import team.aliens.dms.domain.daybreak.service.DaybreakService
import java.time.LocalDate
import java.util.UUID

class QueryGeneralTeacherDaybreakStudyApplicationUseCaseTest : DescribeSpec({

    val daybreakService = mockk<DaybreakService>()
    val securityService = mockk<SecurityService>()

    val useCase = QueryGeneralTeacherDaybreakStudyApplicationUseCase(daybreakService, securityService)

    describe("execute") {
        context("담당 선생님이 새벽 자습 목록을 조회하면") {

            val teacherId = UUID.randomUUID()
            val typeId = UUID.randomUUID()
            val date = LocalDate.now()
            val pageData = PageData(page = 1, size = 10)

            every { securityService.getCurrentUserId() } returns teacherId
            every { daybreakService.generalTeacherGetDaybreakStudyApplications(teacherId, typeId, date, pageData) } returns mockk()

            it("새벽 자습 신청 목록을 반환한다") {
                shouldNotThrowAny {

                    val response = useCase.execute(
                        typeId = typeId,
                        date = date,
                        pageData = pageData
                    )

                    response shouldNotBe null
                }
            }
        }
    }
})
