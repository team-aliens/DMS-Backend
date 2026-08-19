package team.aliens.dms.domain.auth.usecase

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.data.forAll
import io.kotest.data.row
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import io.mockk.verify
import team.aliens.dms.common.service.security.SecurityService
import team.aliens.dms.common.spi.EventPort
import team.aliens.dms.domain.auth.dto.SignInRequest
import team.aliens.dms.domain.auth.dto.TokenResponse
import team.aliens.dms.domain.auth.spi.JwtPort
import team.aliens.dms.domain.school.service.SchoolService
import team.aliens.dms.domain.school.stub.createAvailableFeatureStub
import team.aliens.dms.domain.user.service.UserService
import team.aliens.dms.domain.user.stub.createUserStub
import java.time.LocalDateTime
import java.util.UUID

class SignInUseCaseTest : DescribeSpec({

    val tokenResponse = TokenResponse(
        accessToken = "access-token",
        accessTokenExpiredAt = LocalDateTime.now().plusHours(1),
        refreshToken = "refresh-token",
        refreshTokenExpiredAt = LocalDateTime.now().plusDays(7)
    )

    describe("execute") {
        context("디바이스 토큰과 함께 로그인하면") {
            val securityService = mockk<SecurityService>()
            val userService = mockk<UserService>()
            val schoolService = mockk<SchoolService>()
            val eventPort = mockk<EventPort>()
            val jwtPort = mockk<JwtPort>()
            val signInUseCase = SignInUseCase(
                securityService = securityService,
                userService = userService,
                schoolService = schoolService,
                eventPort = eventPort,
                jwtPort = jwtPort
            )

            val schoolId = UUID.randomUUID()
            val user = createUserStub(schoolId = schoolId)
            val availableFeature = createAvailableFeatureStub(schoolId = schoolId)
            val request = SignInRequest(
                accountId = user.accountId,
                password = "raw-password",
                deviceToken = "device-token"
            )

            every { userService.queryUserByAccountId(request.accountId) } returns user
            every { securityService.checkIsPasswordMatches(request.password, user.password) } just runs
            every { jwtPort.receiveToken(user.id, user.authority, schoolId) } returns tokenResponse
            every { schoolService.getAvailableFeaturesBySchoolId(schoolId) } returns availableFeature
            every { eventPort.publishSaveDeviceToken(any()) } just runs

            it("디바이스 토큰 저장 이벤트만 발행한다") {
                signInUseCase.execute(request)

                verify(exactly = 1) {
                    eventPort.publishSaveDeviceToken(
                        match {
                            it.userId == user.id &&
                                it.schoolId == schoolId &&
                                it.token == request.deviceToken
                        }
                    )
                }
                confirmVerified(eventPort)
            }
        }

        context("디바이스 토큰이 비어 있으면") {
            val securityService = mockk<SecurityService>()
            val userService = mockk<UserService>()
            val schoolService = mockk<SchoolService>()
            val eventPort = mockk<EventPort>()
            val jwtPort = mockk<JwtPort>()
            val signInUseCase = SignInUseCase(
                securityService = securityService,
                userService = userService,
                schoolService = schoolService,
                eventPort = eventPort,
                jwtPort = jwtPort
            )

            val schoolId = UUID.randomUUID()
            val user = createUserStub(schoolId = schoolId)
            val availableFeature = createAvailableFeatureStub(schoolId = schoolId)

            every { userService.queryUserByAccountId(user.accountId) } returns user
            every { securityService.checkIsPasswordMatches(any(), user.password) } just runs
            every { jwtPort.receiveToken(user.id, user.authority, schoolId) } returns tokenResponse
            every { schoolService.getAvailableFeaturesBySchoolId(schoolId) } returns availableFeature

            it("디바이스 토큰 저장 이벤트를 발행하지 않는다") {
                forAll(
                    row(null),
                    row(""),
                ) { deviceToken ->

                    val request = SignInRequest(
                        accountId = user.accountId,
                        password = "raw-password",
                        deviceToken = deviceToken
                    )

                    signInUseCase.execute(request)

                    verify(exactly = 0) { eventPort.publishSaveDeviceToken(any()) }
                }
            }
        }
    }
})
