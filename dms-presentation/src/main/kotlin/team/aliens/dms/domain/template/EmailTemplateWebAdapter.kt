package team.aliens.dms.domain.template

import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import team.aliens.dms.domain.template.usecase.TemplateResponse
import team.aliens.dms.domain.template.usecase.TemplateUseCase

@RequestMapping("/templates")
@RestController
class EmailTemplateWebAdapter(
    private val templateUseCase: TemplateUseCase
) {

    @GetMapping
    fun getTemplates(): List<TemplateResponse> {
        return templateUseCase.getTemplates()
    }

    @PostMapping
    fun createTemplate(@RequestBody request: TemplateRequest) {
        templateUseCase.create(request.type)
    }

    @PatchMapping
    fun updateTemplate(@RequestBody request: TemplateRequest) {
        templateUseCase.update(request.type)
    }

    @DeleteMapping
    fun deleteTemplate(@RequestBody request: TemplateRequest) {
        templateUseCase.delete(request.type)
    }
}

data class TemplateRequest(
    val type: String
)
