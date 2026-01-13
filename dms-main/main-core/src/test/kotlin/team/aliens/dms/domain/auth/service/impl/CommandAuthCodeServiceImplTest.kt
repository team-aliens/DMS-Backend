package team.aliens.dms.domain.auth.service.impl

import io.kotest.assertions.throwables.shouldNotThrowAny
import io.kotest.core.spec.style.DescribeSpec
import io.mockk.every
import io.mockk.mockk
import team.aliens.dms.domain.auth.model.AuthCodeLimit
import team.aliens.dms.domain.auth.model.EmailType
import team.aliens.dms.domain.auth.spi.CommandAuthCodeLimitPort
import team.aliens.dms.domain.auth.spi.CommandAuthCodePort
import team.aliens.dms.domain.auth.spi.QueryAuthCodeLimitPort
import java.util.UUID

class CommandAuthCodeServiceImplTest : DescribeSpec({

    val commandAuthCodePort = mockk<CommandAuthCodePort>(relaxed = true)
    val commandAuthCodeLimitPort = mockk<CommandAuthCodeLimitPort>(relaxed = true)
    val queryAuthCodeLimitPort = mockk<QueryAuthCodeLimitPort>()
    val commandAuthCodeService = CommandAuthCodeServiceImpl(
        commandAuthCodePort,
        commandAuthCodeLimitPort,
        queryAuthCodeLimitPort
    )

    describe("saveIncreasedAuthCodeLimitByEmailAndType") {
        context("기존 AuthCodeLimit이 존재하면") {
            val email = "student@example.com"
            val type = EmailType.SIGNUP
            val existingAuthCodeLimit = AuthCodeLimit(
                id = UUID.randomUUID(),
                email = email,
                type = type,
                attemptCount = 2,
                isVerified = false,
                expirationTime = 1800
            )

            every {
                queryAuthCodeLimitPort.queryAuthCodeLimitByEmailAndEmailType(email, type)
            } returns existingAuthCodeLimit

            it("attemptCount를 1 증가시켜 저장한다") {
                every { commandAuthCodeLimitPort.saveAuthCodeLimit(any()) } returns mockk()

                shouldNotThrowAny {
                    commandAuthCodeService.saveIncreasedAuthCodeLimitByEmailAndType(email, type)
                }
            }
        }
    }
})
