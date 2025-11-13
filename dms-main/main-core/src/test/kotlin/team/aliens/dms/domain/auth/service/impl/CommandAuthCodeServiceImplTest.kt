package team.aliens.dms.domain.auth.service.impl

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
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
                val savedSlot = slot<AuthCodeLimit>()
                every { commandAuthCodeLimitPort.saveAuthCodeLimit(capture(savedSlot)) } returns mockk()

                commandAuthCodeService.saveIncreasedAuthCodeLimitByEmailAndType(email, type)

                verify(exactly = 1) { commandAuthCodeLimitPort.saveAuthCodeLimit(any()) }
                savedSlot.captured.attemptCount shouldBe 3
                savedSlot.captured.email shouldBe email
                savedSlot.captured.type shouldBe type
            }
        }
    }
})
