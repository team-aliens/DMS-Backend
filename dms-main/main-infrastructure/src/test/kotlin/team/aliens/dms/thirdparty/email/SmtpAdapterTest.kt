package team.aliens.dms.thirdparty.email

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.DescribeSpec
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import io.mockk.verify
import jakarta.mail.internet.MimeMessage
import org.springframework.mail.javamail.JavaMailSender
import org.thymeleaf.context.Context
import org.thymeleaf.spring6.SpringTemplateEngine
import team.aliens.dms.domain.auth.model.EmailType
import team.aliens.dms.thirdparty.email.exception.SendEmailRejectedException

class SmtpAdapterTest : DescribeSpec({

    val javaMailSender = mockk<JavaMailSender>()
    val templateEngine = mockk<SpringTemplateEngine>()

    val smtpAdapter = SmtpAdapter(
        javaMailSender = javaMailSender,
        templateEngine = templateEngine
    )

    afterEach {
        clearAllMocks()
    }

    describe("sendAuthCode") {
        context("인증 코드 이메일 전송 시") {
            val email = "user@example.com"
            val code = "123456"
            val type = EmailType.SIGNUP

            it("정상적으로 이메일을 전송한다") {
                val mimeMessage = mockk<MimeMessage>(relaxed = true)
                val htmlContent = "<html><body>Code: $code</body></html>"

                every { templateEngine.process(type.fileName, any<Context>()) } returns htmlContent
                every { javaMailSender.createMimeMessage() } returns mimeMessage
                every { javaMailSender.send(mimeMessage) } just runs

                smtpAdapter.sendAuthCode(email, type, code)

                verify(exactly = 1) { templateEngine.process(type.fileName, any<Context>()) }
                verify(exactly = 1) { javaMailSender.send(mimeMessage) }
            }

            it("이메일 전송 실패 시 SendEmailRejectedException을 발생시킨다") {
                val mimeMessage = mockk<MimeMessage>(relaxed = true)
                val htmlContent = "<html><body>Code: $code</body></html>"

                every { templateEngine.process(type.fileName, any<Context>()) } returns htmlContent
                every { javaMailSender.createMimeMessage() } returns mimeMessage
                every { javaMailSender.send(mimeMessage) } throws RuntimeException("Send failed")

                shouldThrow<SendEmailRejectedException> {
                    smtpAdapter.sendAuthCode(email, type, code)
                }
            }
        }
    }

    describe("sendAccountId") {
        context("계정 ID 이메일 전송 시") {
            val email = "user@example.com"
            val accountId = "testuser123"

            it("정상적으로 이메일을 전송한다") {
                val mimeMessage = mockk<MimeMessage>(relaxed = true)
                val htmlContent = "<html><body>Your ID: $accountId</body></html>"

                every {
                    templateEngine.process(EmailType.FIND_ACCOUNT_ID.fileName, any<Context>())
                } returns htmlContent
                every { javaMailSender.createMimeMessage() } returns mimeMessage
                every { javaMailSender.send(mimeMessage) } just runs

                smtpAdapter.sendAccountId(email, accountId)

                verify(exactly = 1) {
                    templateEngine.process(EmailType.FIND_ACCOUNT_ID.fileName, any<Context>())
                }
                verify(exactly = 1) { javaMailSender.send(mimeMessage) }
            }

            it("이메일 전송 실패 시 SendEmailRejectedException을 발생시킨다") {
                val mimeMessage = mockk<MimeMessage>(relaxed = true)
                val htmlContent = "<html><body>Your ID: $accountId</body></html>"

                every {
                    templateEngine.process(EmailType.FIND_ACCOUNT_ID.fileName, any<Context>())
                } returns htmlContent
                every { javaMailSender.createMimeMessage() } returns mimeMessage
                every { javaMailSender.send(mimeMessage) } throws RuntimeException("Send failed")

                shouldThrow<SendEmailRejectedException> {
                    smtpAdapter.sendAccountId(email, accountId)
                }
            }
        }
    }
})
