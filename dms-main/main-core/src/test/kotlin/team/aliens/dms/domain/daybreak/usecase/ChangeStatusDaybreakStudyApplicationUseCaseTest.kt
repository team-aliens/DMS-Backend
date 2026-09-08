package team.aliens.dms.domain.daybreak.usecase

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.data.forAll
import io.kotest.data.row
import io.kotest.matchers.shouldBe
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import io.mockk.verify
import team.aliens.dms.common.service.security.SecurityService
import team.aliens.dms.domain.auth.model.Authority
import team.aliens.dms.domain.daybreak.dto.request.ChangeDaybreakStudyApplicationStatusRequest
import team.aliens.dms.domain.daybreak.model.Status
import team.aliens.dms.domain.daybreak.service.DaybreakService
import team.aliens.dms.domain.daybreak.stub.createDaybreakStudyApplicationStub
import team.aliens.dms.domain.user.exception.InvalidRoleException
import java.util.UUID

class ChangeStatusDaybreakStudyApplicationUseCaseTest : DescribeSpec({

    val daybreakService = mockk<DaybreakService>()
    val securityService = mockk<SecurityService>()

    val useCase = ChangeStatusDaybreakStudyApplicationUseCase(daybreakService, securityService)

    describe("execute") {
        context("권한에 따른 올바른 상태 변경 요청이 왔다면") {
            it("권한과 요청 상태에 따라 새벽 자습 신청 상태를 바꾸고 저장한다") {
                val applicationId = UUID.randomUUID()

                forAll(
                    // 담당 선생님: PENDING → FIRST_APPROVED / REJECTED
                    row(Authority.GENERAL_TEACHER, Status.PENDING, Status.FIRST_APPROVED),
                    row(Authority.GENERAL_TEACHER, Status.PENDING, Status.REJECTED),

                    // 부장 선생님: FIRST_APPROVED → SECOND_APPROVED / REJECTED
                    row(Authority.HEAD_TEACHER, Status.FIRST_APPROVED, Status.SECOND_APPROVED),
                    row(Authority.HEAD_TEACHER, Status.FIRST_APPROVED, Status.REJECTED),
                ) { authority, startingStatus, requestStatus ->

                    // given
                    val application = createDaybreakStudyApplicationStub(id = applicationId, status = startingStatus)
                    val request = ChangeDaybreakStudyApplicationStatusRequest(
                        applicationIdList = listOf(applicationId),
                        status = requestStatus
                    )
                    every { securityService.getCurrentUserAuthority() } returns authority
                    every { daybreakService.getAllByIdIn(request.applicationIdList) } returns listOf(application)
                    every { daybreakService.saveAllDaybreakStudyApplications(any()) } just runs

                    // when
                    useCase.execute(request)

                    // then
                    application.status shouldBe requestStatus
                    application.previousStatus shouldBe startingStatus

                    verify(exactly = 1) {
                        daybreakService.saveAllDaybreakStudyApplications(listOf(application))
                    }

                    clearAllMocks()
                }
            }
        }
        context("권한에 따른 옳지 않은 상태 변경 요청이 왔다면") {
            it("도메인 모델의 실제 검증 규칙에 따라 예외를 던지고 저장하지 않는다") {
                val applicationId = UUID.randomUUID()
                // 담당 선생님(GENERAL_TEACHER)은 PENDING에서 SECOND_APPROVED로 못 바꾼다 — 실제 changeStatus()가 던지는 예외
                val application = createDaybreakStudyApplicationStub(id = applicationId, status = Status.PENDING)

                val authority = Authority.GENERAL_TEACHER
                val illegalStatus = Status.SECOND_APPROVED
                val request = ChangeDaybreakStudyApplicationStatusRequest(
                    applicationIdList = listOf(applicationId),
                    status = illegalStatus
                )

                every { securityService.getCurrentUserAuthority() } returns authority
                every { daybreakService.getAllByIdIn(request.applicationIdList) } returns listOf(application)

                shouldThrow<InvalidRoleException> {
                    useCase.execute(request)
                }

                verify(exactly = 0) { daybreakService.saveAllDaybreakStudyApplications(any()) }
            }
        }
    }
})
