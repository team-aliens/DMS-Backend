package team.aliens.dms.domain.daybreak.usecase

import io.kotest.core.spec.style.DescribeSpec
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import io.mockk.verify
import team.aliens.dms.common.service.security.SecurityService
import team.aliens.dms.domain.daybreak.dto.request.CreateDaybreakStudyTypeRequest
import team.aliens.dms.domain.daybreak.service.DaybreakService
import java.util.UUID

class CreateDaybreakStudyTypeUseCaseTest : DescribeSpec ({

    val daybreakService = mockk<DaybreakService>()
    val securityService = mockk<SecurityService>()

    val useCase = CreateDaybreakStudyTypeUseCase(daybreakService, securityService)

    describe("execute"){
        context("올바른 요청이 들어오면"){
            val schoolId = UUID.randomUUID()

            val request = CreateDaybreakStudyTypeRequest(
                name = "개인 프로젝트"
            )
            it("새벽 자습 유형을 저장한다") {

                // given
                every { securityService.getCurrentSchoolId() } returns schoolId
                every { daybreakService.checkDaybreakStudyTypeExists(schoolId, request.name) } just runs
                every { daybreakService.saveDaybreakStudyType(any()) } just runs

                // when
                useCase.execute(request)

                // then
                verify(exactly = 1) {
                    daybreakService.checkDaybreakStudyTypeExists(schoolId, request.name)
                }
                verify(exactly = 1) {
                    daybreakService.saveDaybreakStudyType(any())
                }
            }

        }
    }
})
