package team.aliens.dms.thirdparty.email

import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceAsync
import com.amazonaws.services.simpleemail.model.CreateTemplateRequest
import com.amazonaws.services.simpleemail.model.DeleteTemplateRequest
import com.amazonaws.services.simpleemail.model.Destination
import com.amazonaws.services.simpleemail.model.ListTemplatesRequest
import com.amazonaws.services.simpleemail.model.SendTemplatedEmailRequest
import com.amazonaws.services.simpleemail.model.Template
import com.amazonaws.services.simpleemail.model.UpdateTemplateRequest
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.stereotype.Component
import org.thymeleaf.context.Context
import org.thymeleaf.spring6.SpringTemplateEngine
import team.aliens.dms.domain.auth.model.EmailType
import team.aliens.dms.domain.auth.spi.SendEmailPort
import team.aliens.dms.domain.template.spi.TemplatePort
import team.aliens.dms.domain.template.usecase.TemplateResponse
import team.aliens.dms.thirdparty.email.exception.SendEmailRejectedException
import team.aliens.dms.thirdparty.email.exception.SesException
import java.time.ZoneId

@Component
class AwsSESAdapter(
    private val amazonSimpleEmailServiceAsync: AmazonSimpleEmailServiceAsync,
    private val awsSESProperties: AwsSESProperties,
    private val templateEngine: SpringTemplateEngine,
    private val objectMapper: ObjectMapper
) : SendEmailPort, TemplatePort {

    override fun queryTemplates(): List<TemplateResponse> {
        val response = amazonSimpleEmailServiceAsync.listTemplates(
            ListTemplatesRequest().withMaxItems(10)
        ).templatesMetadata

        return response.map {
            TemplateResponse(
                name = it.name,
                createdAt = it.createdTimestamp.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime()
            )
        }
    }

    override fun sendAuthCode(email: String, type: EmailType, code: String) {
        val data = mutableMapOf(
            "code" to code
        )

        val templatedEmailRequest = SendTemplatedEmailRequest()
            .withDestination(Destination().withToAddresses(email))
            .withTemplate(type.templateName)
            .withTemplateData(paramToJson(data))
            .withSource(awsSESProperties.source)

        runCatching {
            amazonSimpleEmailServiceAsync.sendTemplatedEmailAsync(templatedEmailRequest)
        }.onFailure { e ->
            e.printStackTrace()
            throw SendEmailRejectedException
        }
    }

    override fun sendAccountId(email: String, accountId: String) {
        val data = mutableMapOf(
            "accountId" to accountId
        )

        val templatedEmailRequest = SendTemplatedEmailRequest()
            .withDestination(Destination().withToAddresses(email))
            .withTemplate(EmailType.FIND_ACCOUNT_ID.templateName)
            .withTemplateData(paramToJson(data))
            .withSource(awsSESProperties.source)

        runCatching {
            amazonSimpleEmailServiceAsync.sendTemplatedEmailAsync(templatedEmailRequest)
        }.onFailure { e ->
            e.printStackTrace()
            throw SendEmailRejectedException
        }
    }

    private fun paramToJson(
        params: Map<String, String>
    ) = objectMapper.writeValueAsString(params).replace("\"".toRegex(), "\\\"")

    override fun createTemplate(type: EmailType) {
        val html = templateEngine.process(type.fileName, Context())

        val template = Template()
            .withTemplateName(type.templateName)
            .withSubjectPart(type.templateSubject)
            .withHtmlPart(html)

        runCatching {
            amazonSimpleEmailServiceAsync.createTemplate(
                CreateTemplateRequest().withTemplate(template)
            )
        }.onFailure { e ->
            e.printStackTrace()
            throw SesException
        }
    }

    override fun updateTemplate(type: EmailType) {
        val html = templateEngine.process(type.fileName, Context())

        val template = Template()
            .withTemplateName(type.templateName)
            .withSubjectPart(type.templateSubject)
            .withHtmlPart(html)

        runCatching {
            amazonSimpleEmailServiceAsync.updateTemplate(
                UpdateTemplateRequest().withTemplate(template)
            )
        }.onFailure { e ->
            e.printStackTrace()
            throw SesException
        }
    }

    override fun deleteTemplate(type: EmailType) {
        runCatching {
            amazonSimpleEmailServiceAsync.deleteTemplate(
                DeleteTemplateRequest().withTemplateName(type.templateName)
            )
        }.onFailure { e ->
            e.printStackTrace()
            throw SesException
        }
    }
}
