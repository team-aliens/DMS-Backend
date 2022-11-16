package team.aliens.dms.domain

import team.aliens.dms.common.annotation.UseCase
import team.aliens.dms.domain.auth.model.EmailType

@UseCase
class TemplateUseCase(
    private val commandTemplatePort: CommandTemplatePort
) {

    fun create(type: String) {
        commandTemplatePort.createTemplate(
            EmailType.valueOf(type)
        )
    }

    fun update(type: String) {
        commandTemplatePort.updateTemplate(
            EmailType.valueOf(type)
        )
    }

    fun delete(type: String) {
        commandTemplatePort.deleteTemplate(
            EmailType.valueOf(type)
        )
    }
}