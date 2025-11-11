package team.aliens.dms.thirdparty.email

import jakarta.mail.internet.MimeMessage
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.stereotype.Component
import org.thymeleaf.context.Context
import org.thymeleaf.spring6.SpringTemplateEngine
import team.aliens.dms.domain.auth.model.EmailType
import team.aliens.dms.domain.auth.spi.SendEmailPort
import team.aliens.dms.thirdparty.email.exception.SendEmailRejectedException

@Component
class SmtpAdapter(
    private val javaMailSender: JavaMailSender,
    private val templateEngine: SpringTemplateEngine
) : SendEmailPort {

    override fun sendAuthCode(email: String, type: EmailType, code: String) {
        val context = Context().apply {
            setVariable("code", code)
        }

        val htmlContent = templateEngine.process(type.fileName, context)

        runCatching {
            sendHtmlEmail(
                to = email,
                subject = type.templateSubject,
                htmlContent = htmlContent
            )
        }.onFailure { e ->
            e.printStackTrace()
            throw SendEmailRejectedException
        }
    }

    override fun sendAccountId(email: String, accountId: String) {
        val context = Context().apply {
            setVariable("accountId", accountId)
        }

        val htmlContent = templateEngine.process(
            EmailType.FIND_ACCOUNT_ID.fileName,
            context
        )

        runCatching {
            sendHtmlEmail(
                to = email,
                subject = EmailType.FIND_ACCOUNT_ID.templateSubject,
                htmlContent = htmlContent
            )
        }.onFailure { e ->
            e.printStackTrace()
            throw SendEmailRejectedException
        }
    }

    private fun sendHtmlEmail(to: String, subject: String, htmlContent: String) {
        val message: MimeMessage = javaMailSender.createMimeMessage()
        val helper = MimeMessageHelper(message, true, "UTF-8")

        helper.setTo(to)
        helper.setSubject(subject)
        helper.setText(htmlContent, true)

        javaMailSender.send(message)
    }
}
