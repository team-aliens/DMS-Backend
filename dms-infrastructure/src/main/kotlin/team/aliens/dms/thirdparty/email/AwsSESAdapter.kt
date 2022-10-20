package team.aliens.dms.thirdparty.email

import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceAsync
import com.amazonaws.services.simpleemail.model.Destination
import com.amazonaws.services.simpleemail.model.MessageRejectedException
import com.amazonaws.services.simpleemail.model.SendTemplatedEmailRequest
import org.springframework.stereotype.Component
import team.aliens.dms.domain.auth.model.EmailType
import team.aliens.dms.domain.auth.spi.SendAuthPort
import team.aliens.dms.thirdparty.email.exception.SendEmailRejectedException

@Component
class AwsSESAdapter(
    private val amazonSimpleEmailServiceAsync : AmazonSimpleEmailServiceAsync,
    private val awsSESProperties: AwsSESProperties
) : SendAuthPort {

    override fun sendEmailCode(email: String, type: EmailType, code: String) {
        val templateName = type.templateName

        val request = SendTemplatedEmailRequest()
            .withDestination(Destination().withToAddresses(email))
            .withTemplate(templateName)
            .withTemplateData(code)
            .withSource(awsSESProperties.source)

        try {
            amazonSimpleEmailServiceAsync.sendTemplatedEmailAsync(request)
        } catch (e : MessageRejectedException) {
            throw SendEmailRejectedException
        }
    }

}