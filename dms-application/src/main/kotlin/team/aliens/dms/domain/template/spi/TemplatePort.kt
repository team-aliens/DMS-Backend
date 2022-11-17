package team.aliens.dms.domain.template.spi

import team.aliens.dms.domain.auth.model.EmailType
import team.aliens.dms.domain.template.usecase.TemplateResponse

interface TemplatePort {

    fun queryTemplates(): List<TemplateResponse>

    fun createTemplate(type: EmailType)

    fun updateTemplate(type: EmailType)

    fun deleteTemplate(type: EmailType)

}