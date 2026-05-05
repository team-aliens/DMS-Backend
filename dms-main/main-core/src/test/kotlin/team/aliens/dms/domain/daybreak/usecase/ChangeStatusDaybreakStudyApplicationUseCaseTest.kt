package team.aliens.dms.domain.daybreak.usecase

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.data.forAll
import io.kotest.data.row
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import io.mockk.verify
import team.aliens.dms.common.service.security.SecurityService
import team.aliens.dms.domain.auth.model.Authority
import team.aliens.dms.domain.daybreak.dto.request.ChangeDaybreakStudyApplicationStatusRequest
import team.aliens.dms.domain.daybreak.model.DaybreakStudyApplication
import team.aliens.dms.domain.daybreak.model.Status
import team.aliens.dms.domain.daybreak.service.DaybreakService
import team.aliens.dms.domain.user.exception.InvalidRoleException
import java.util.UUID

class ChangeStatusDaybreakStudyApplicationUseCaseTest : DescribeSpec({

    val daybreakService = mockk<DaybreakService>()
    val securityService = mockk<SecurityService>()

    val useCase = ChangeStatusDaybreakStudyApplicationUseCase(daybreakService, securityService)

    describe("execute") {
        context("권한에 따른 올바른 상태 변경 요청이 왔다면") {
            it("권한과 요청 상태에 따라 새벽 자습 신청 상태를 바꾼다") {
                val applicationId = UUID.randomUUID()
                val mockApplication = mockk<DaybreakStudyApplication>(relaxed = true)

                forAll(
                    // 담당 선생님
                    row(Authority.GENERAL_TEACHER, Status.FIRST_APPROVED),
                    row(Authority.GENERAL_TEACHER, Status.REJECTED),

                    // 부장 선생님
                    row(Authority.HEAD_TEACHER, Status.SECOND_APPROVED),
                    row(Authority.HEAD_TEACHER, Status.REJECTED),
                ) { authority, requestStatus ->

                    // given
                    val request = ChangeDaybreakStudyApplicationStatusRequest(
                        applicationIdList = listOf(applicationId),
                        status = requestStatus
                    )

                    every { securityService.getCurrentUserAuthority() } returns authority
                    every { daybreakService.getAllByIdIn(request.applicationIdList) } returns listOf(mockApplication)
                    every { daybreakService.saveAllDaybreakStudyApplications(any()) } just runs

                    // when
                    useCase.execute(request)

                    // then
                    verify(exactly = 1) {
                        mockApplication.changeStatus(authority, requestStatus)
                    }
                    verify(exactly = 1) {
                        daybreakService.saveAllDaybreakStudyApplications(any())
                    }

                    clearAllMocks()
                }
            }
        }
        context("권한에 따른 옳지 않는 상태 변경 요청이 왔다면") {
            it("예외를 반환한다") {
                val applicationId = UUID.randomUUID()
                val mockApplication = mockk<DaybreakStudyApplication>(relaxed = true)

                // given
                val authority = Authority.GENERAL_TEACHER
                val illegalStatus = Status.SECOND_APPROVED
                val request = ChangeDaybreakStudyApplicationStatusRequest(
                    applicationIdList = listOf(applicationId),
                    status = illegalStatus
                )

                every { securityService.getCurrentUserAuthority() } returns authority
                every { daybreakService.getAllByIdIn(request.applicationIdList) } returns listOf(mockApplication)

                every { mockApplication.changeStatus(authority, illegalStatus) } throws InvalidRoleException

                // when & then
                shouldThrow<InvalidRoleException> {
                    useCase.execute(request)
                }
            }
        }
    }
})
