package team.aliens.dms.domain.daybreak.usecase

import io.kotest.assertions.throwables.shouldNotThrowAny
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldNotBe
import io.mockk.every
import io.mockk.mockk
import team.aliens.dms.common.service.security.SecurityService
import team.aliens.dms.domain.daybreak.service.DaybreakService
import java.util.UUID

class QueryDaybreakStudyTypesUseCaseTest : DescribeSpec({

    val daybreakService = mockk<DaybreakService>()
    val securityService = mockk<SecurityService>()

    val useCase = QueryDaybreakStudyTypesUseCase(daybreakService, securityService)

    describe("execute") {
        context("새벽 자습 유형 조회를 요청하면") {
            val schoolId = UUID.randomUUID()

            every { securityService.getCurrentSchoolId() } returns schoolId
            every { daybreakService.getDaybreakStudyTypesBySchoolId(schoolId) } returns emptyList()

            it("자습 유형 응답 객체를 반환한다") {
                shouldNotThrowAny {
                    val response = useCase.execute()

                    response shouldNotBe null
                }
            }
        }
    }
})
