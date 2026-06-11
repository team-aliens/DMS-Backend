package team.aliens.dms.domain.daybreak.usecase

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import io.mockk.verify
import team.aliens.dms.domain.daybreak.model.Status
import team.aliens.dms.domain.daybreak.service.DaybreakService
import team.aliens.dms.domain.daybreak.stub.createDaybreakStudyApplicationStub

class CloseDaybreakStudyApplicationUseCaseTest : DescribeSpec({

    val daybreakService = mockk<DaybreakService>()

    val useCase = CloseDaybreakStudyApplicationUseCase(daybreakService)

    afterTest { clearAllMocks() }

    describe("execute") {
        context("마감 스케줄러가 실행되면") {
            it("SECOND_APPROVED 신청의 상태를 EXPIRED로 변경하고 미처리 신청을 삭제한다") {
                val applications = listOf(
                    createDaybreakStudyApplicationStub(status = Status.SECOND_APPROVED),
                    createDaybreakStudyApplicationStub(status = Status.SECOND_APPROVED),
                )

                every { daybreakService.findExpiredDaybreakStudyApplications() } returns applications
                every { daybreakService.saveAllDaybreakStudyApplications(any()) } just runs
                every { daybreakService.deleteOutdatedDaybreakStudyApplications() } just runs

                useCase.execute()

                applications.forEach { it.status shouldBe Status.EXPIRED }
                verify(exactly = 1) { daybreakService.saveAllDaybreakStudyApplications(applications) }
                verify(exactly = 1) { daybreakService.deleteOutdatedDaybreakStudyApplications() }
            }
        }

        context("만료 대상 신청이 없으면") {
            it("빈 리스트로 저장하고 미처리 신청을 삭제한다") {
                every { daybreakService.findExpiredDaybreakStudyApplications() } returns emptyList()
                every { daybreakService.saveAllDaybreakStudyApplications(any()) } just runs
                every { daybreakService.deleteOutdatedDaybreakStudyApplications() } just runs

                useCase.execute()

                verify(exactly = 1) { daybreakService.saveAllDaybreakStudyApplications(emptyList()) }
                verify(exactly = 1) { daybreakService.deleteOutdatedDaybreakStudyApplications() }
            }
        }
    }
})
