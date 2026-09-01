package team.aliens.dms.domain.daybreak.usecase

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.data.forAll
import io.kotest.data.row
import io.kotest.matchers.shouldBe
import io.mockk.clearAllMocks
import io.mockk.clearMocks
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import io.mockk.verify
import team.aliens.dms.common.service.security.SecurityService
import team.aliens.dms.domain.daybreak.dto.request.RevertDaybreakStudyApplicationRequest
import team.aliens.dms.domain.daybreak.exception.DaybreakStudentSchoolMismatchException
import team.aliens.dms.domain.daybreak.exception.DaybreakStudyApplicationCanNotRevertException
import team.aliens.dms.domain.daybreak.exception.DaybreakStudyApplicationNotFoundException
import team.aliens.dms.domain.daybreak.model.Status
import team.aliens.dms.domain.daybreak.service.DaybreakService
import team.aliens.dms.domain.daybreak.stub.createDaybreakStudyApplicationStub
import java.util.UUID

class RevertDaybreakStudyApplicationUseCaseTest : DescribeSpec({
    val daybreakService = mockk<DaybreakService>()
    val securityService = mockk<SecurityService>()

    val useCase = RevertDaybreakStudyApplicationUseCase(daybreakService, securityService)

    beforeTest {
        clearMocks(daybreakService, securityService)
    }

    describe("execute"){

        context("최증 승인(SECOND_APPROVED) 또는 거절(REJECTED)한 새벽자습 신청이라면"){

            it("모두 FIRST_APPROVED로 되돌려 저장한다"){
                val schoolId = UUID.randomUUID()
                val applicationIdList = listOf(UUID.randomUUID(), UUID.randomUUID())

                val applications = listOf(
                    createDaybreakStudyApplicationStub(
                        id = applicationIdList[0],
                        status = Status.SECOND_APPROVED,
                        schoolId = schoolId
                    ),
                    createDaybreakStudyApplicationStub(
                        id = applicationIdList[1],
                        status = Status.REJECTED,
                        schoolId = schoolId
                    ),
                )

                every { securityService.getCurrentSchoolId() } returns schoolId
                every { daybreakService.getAllByIdIn(applicationIdList) } returns applications
                every { daybreakService.saveAllDaybreakStudyApplications(any()) } just runs

                useCase.execute(RevertDaybreakStudyApplicationRequest(applicationIdList))

                applications.forEach { it.status shouldBe Status.FIRST_APPROVED }
                verify(exactly = 1) {
                    daybreakService.saveAllDaybreakStudyApplications(
                        match { list -> list.all { it.status == Status.FIRST_APPROVED } }
                    )
                }
            }
        }

        context("되돌릴 수 없는 상태(PENDING, FIRST_APPROVED, EXPIRED)의 신청이 포함되면"){

            it("DaybreakStudyApplicationCanNotRevertException을 던지고 저장하지 않는다"){
                forAll(
                    row(Status.PENDING),
                    row(Status.FIRST_APPROVED),
                    row(Status.EXPIRED),
                ) { status ->
                    val schoolId = UUID.randomUUID()
                    val applicationId = UUID.randomUUID()
                    val applications = listOf(
                        createDaybreakStudyApplicationStub(id = applicationId, status = status, schoolId = schoolId)
                    )
                    every { securityService.getCurrentSchoolId() } returns schoolId
                    every { daybreakService.getAllByIdIn(listOf(applicationId)) } returns applications

                    shouldThrow<DaybreakStudyApplicationCanNotRevertException> {
                        useCase.execute(RevertDaybreakStudyApplicationRequest(listOf(applicationId)))
                    }

                    verify(exactly = 0) { daybreakService.saveAllDaybreakStudyApplications(any()) }

                    clearAllMocks()
                }
            }
        }

        context("존재하지 않는 신청 id가 포함되면"){

            it("DaybreakStudyApplicationNotFoundException을 그대로 전파하고 저장하지 않는다"){
                val applicationIdList = listOf(UUID.randomUUID(), UUID.randomUUID())

                every { securityService.getCurrentSchoolId() } returns UUID.randomUUID()

                every {
                    daybreakService.getAllByIdIn(applicationIdList)
                } throws DaybreakStudyApplicationNotFoundException

                shouldThrow<DaybreakStudyApplicationNotFoundException> {
                    useCase.execute(RevertDaybreakStudyApplicationRequest(applicationIdList))
                }
                verify(exactly = 0) { daybreakService.saveAllDaybreakStudyApplications(any()) }
            }
        }

    }
})
