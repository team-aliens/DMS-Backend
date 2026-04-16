package team.aliens.dms.domain.daybreak.usecase

import io.kotest.assertions.throwables.shouldNotThrowAny
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldNotBe
import io.mockk.every
import io.mockk.mockk
import team.aliens.dms.common.dto.PageData
import team.aliens.dms.domain.daybreak.service.DaybreakService

class QueryManagerDaybreakStudyApplicationUseCaseTest : DescribeSpec({
    val daybreakService = mockk<DaybreakService>()

    val useCase = QueryManagerDaybreakStudyApplicationUseCase(daybreakService)

    describe("execute"){
        context("사감 선생님이 새벽 자습 신청 목록을 조회하면"){

            val pageData = PageData(page = 1, size = 10)
            val grade = 1

            every { daybreakService.managerGetDaybreakStudyApplications(grade, pageData) } returns mockk()

            it("새벽 자습 신청 목록을 반환한다"){
                shouldNotThrowAny {

                    val response = useCase.execute(
                        grade = grade,
                        pageData = pageData
                    )

                    response shouldNotBe null
                }
            }
        }
    }

})