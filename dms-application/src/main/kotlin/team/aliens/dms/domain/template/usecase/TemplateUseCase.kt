package team.aliens.dms.domain.template.usecase

import team.aliens.dms.common.annotation.UseCase
import team.aliens.dms.domain.auth.model.EmailType
import team.aliens.dms.domain.template.spi.TemplatePort
import java.time.LocalDateTime

@UseCase
class TemplateUseCase(
    private val templatePort: TemplatePort
) {

    fun getTemplates(): List<TemplateResponse> {
        return templatePort.queryTemplates()
    }

    fun create(type: String) {
        templatePort.createTemplate(
            EmailType.valueOf(type)
        )
    }

    fun update(type: String) {
        templatePort.updateTemplate(
            EmailType.valueOf(type)
        )
    }

    fun delete(type: String) {
        templatePort.deleteTemplate(
            EmailType.valueOf(type)
        )
    }
}

data class TemplateResponse(
    val name: String,
    val createdAt: LocalDateTime
)
