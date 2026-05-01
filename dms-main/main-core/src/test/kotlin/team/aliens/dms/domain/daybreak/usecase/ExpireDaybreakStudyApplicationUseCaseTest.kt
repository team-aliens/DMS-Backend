package team.aliens.dms.domain.daybreak.usecase

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import io.mockk.verify
import team.aliens.dms.domain.daybreak.model.Status
import team.aliens.dms.domain.daybreak.service.DaybreakService
import team.aliens.dms.domain.daybreak.stub.createDaybreakStudyApplicationStub

class ExpireDaybreakStudyApplicationUseCaseTest : DescribeSpec({

    val daybreakService = mockk<DaybreakService>()

    val useCase = ExpireDaybreakStudyApplicationUseCase(daybreakService)

    describe("execute") {
        context("만료 스케줄러가 실행되면") {
            it("기간이 지난 새벽 자습 신청의 상태를 EXPIRED로 바꾼다") {
                val applications = listOf(
                    createDaybreakStudyApplicationStub(status = Status.PENDING),
                    createDaybreakStudyApplicationStub(status = Status.FIRST_APPROVED),
                    createDaybreakStudyApplicationStub(status = Status.SECOND_APPROVED),
                    createDaybreakStudyApplicationStub(status = Status.REJECTED),
                )

                every { daybreakService.findExpiredDaybreakStudyApplications() } returns applications
                every { daybreakService.saveAllDaybreakStudyApplications(any()) } just runs

                useCase.execute()

                applications.forEach { it.status shouldBe Status.EXPIRED }
                verify(exactly = 1) { daybreakService.saveAllDaybreakStudyApplications(applications) }
            }
        }

        context("만료 대상 신청이 없다면") {
            it("빈 리스트로 저장을 호출한다") {
                every { daybreakService.findExpiredDaybreakStudyApplications() } returns emptyList()
                every { daybreakService.saveAllDaybreakStudyApplications(any()) } just runs

                useCase.execute()

                verify(exactly = 1) { daybreakService.saveAllDaybreakStudyApplications(emptyList()) }
            }
        }
    }
})
