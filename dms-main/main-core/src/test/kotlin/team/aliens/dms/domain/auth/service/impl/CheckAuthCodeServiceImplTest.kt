package team.aliens.dms.domain.auth.service.impl

import io.kotest.assertions.throwables.shouldNotThrowAny
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.DescribeSpec
import io.mockk.every
import io.mockk.mockk
import team.aliens.dms.domain.auth.exception.AuthCodeMismatchException
import team.aliens.dms.domain.auth.exception.AuthCodeNotFoundException
import team.aliens.dms.domain.auth.exception.UnverifiedAuthCodeException
import team.aliens.dms.domain.auth.model.AuthCode
import team.aliens.dms.domain.auth.model.AuthCodeLimit
import team.aliens.dms.domain.auth.model.EmailType
import team.aliens.dms.domain.auth.spi.QueryAuthCodeLimitPort
import team.aliens.dms.domain.auth.spi.QueryAuthCodePort
import java.util.UUID

class CheckAuthCodeServiceImplTest : DescribeSpec({

    val queryAuthCodePort = mockk<QueryAuthCodePort>()
    val queryAuthCodeLimitPort = mockk<QueryAuthCodeLimitPort>()
    val checkAuthCodeService = CheckAuthCodeServiceImpl(queryAuthCodePort, queryAuthCodeLimitPort)

    describe("checkAuthCodeExists") {
        val code = "123456"

        context("인증 코드가 존재하면") {
            every { queryAuthCodePort.existsAuthCodeByCode(code) } returns true

            it("예외가 발생하지 않는다") {
                shouldNotThrowAny {
                    checkAuthCodeService.checkAuthCodeExists(code)
                }
            }
        }

        context("인증 코드가 존재하지 않으면") {
            every { queryAuthCodePort.existsAuthCodeByCode(code) } returns false

            it("AuthCodeMismatchException이 발생한다") {
                shouldThrow<AuthCodeMismatchException> {
                    checkAuthCodeService.checkAuthCodeExists(code)
                }
            }
        }
    }

    describe("checkAuthCodeByEmail") {
        val email = "student@example.com"
        val correctCode = "123456"
        val wrongCode = "654321"

        context("이메일에 해당하는 인증 코드가 존재하고 코드가 일치하면") {
            val authCode = AuthCode(
                code = correctCode,
                email = email,
                type = EmailType.SIGNUP
            )

            every { queryAuthCodePort.queryAuthCodeByEmail(email) } returns authCode

            it("예외가 발생하지 않는다") {
                shouldNotThrowAny {
                    checkAuthCodeService.checkAuthCodeByEmail(email, correctCode)
                }
            }
        }

        context("이메일에 해당하는 인증 코드가 존재하지만 코드가 일치하지 않으면") {
            val authCode = AuthCode(
                code = correctCode,
                email = email,
                type = EmailType.SIGNUP
            )

            every { queryAuthCodePort.queryAuthCodeByEmail(email) } returns authCode

            it("AuthCodeMismatchException이 발생한다") {
                shouldThrow<AuthCodeMismatchException> {
                    checkAuthCodeService.checkAuthCodeByEmail(email, wrongCode)
                }
            }
        }

        context("이메일에 해당하는 인증 코드가 존재하지 않으면") {
            every { queryAuthCodePort.queryAuthCodeByEmail(email) } returns null

            it("AuthCodeNotFoundException이 발생한다") {
                shouldThrow<AuthCodeNotFoundException> {
                    checkAuthCodeService.checkAuthCodeByEmail(email, correctCode)
                }
            }
        }
    }

    describe("checkAuthCodeLimitIsVerifiedByEmail") {
        val email = "student@example.com"

        context("인증 코드 제한이 존재하고 인증이 완료되었으면") {
            val verifiedAuthCodeLimit = AuthCodeLimit(
                id = UUID.randomUUID(),
                email = email,
                type = EmailType.SIGNUP,
                attemptCount = 1,
                isVerified = true,
                expirationTime = 1800
            )

            every { queryAuthCodeLimitPort.queryAuthCodeLimitByEmail(email) } returns verifiedAuthCodeLimit

            it("예외가 발생하지 않는다") {
                shouldNotThrowAny {
                    checkAuthCodeService.checkAuthCodeLimitIsVerifiedByEmail(email)
                }
            }
        }

        context("인증 코드 제한이 존재하지만 인증이 완료되지 않았으면") {
            val unverifiedAuthCodeLimit = AuthCodeLimit(
                id = UUID.randomUUID(),
                email = email,
                type = EmailType.SIGNUP,
                attemptCount = 1,
                isVerified = false,
                expirationTime = 1800
            )

            every { queryAuthCodeLimitPort.queryAuthCodeLimitByEmail(email) } returns unverifiedAuthCodeLimit

            it("UnverifiedAuthCodeException이 발생한다") {
                shouldThrow<UnverifiedAuthCodeException> {
                    checkAuthCodeService.checkAuthCodeLimitIsVerifiedByEmail(email)
                }
            }
        }

        context("인증 코드 제한이 존재하지 않으면") {
            every { queryAuthCodeLimitPort.queryAuthCodeLimitByEmail(email) } returns null

            it("AuthCodeNotFoundException이 발생한다") {
                shouldThrow<AuthCodeNotFoundException> {
                    checkAuthCodeService.checkAuthCodeLimitIsVerifiedByEmail(email)
                }
            }
        }
    }
})
