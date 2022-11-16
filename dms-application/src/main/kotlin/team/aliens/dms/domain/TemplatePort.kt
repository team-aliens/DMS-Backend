package team.aliens.dms.domain

import team.aliens.dms.domain.auth.model.EmailType

interface TemplatePort {

    fun queryTemplates(): List<TemplateResponse>

    fun createTemplate(type: EmailType)

    fun updateTemplate(type: EmailType)

    fun deleteTemplate(type: EmailType)

}