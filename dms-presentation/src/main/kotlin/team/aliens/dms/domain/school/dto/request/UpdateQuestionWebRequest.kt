package team.aliens.dms.domain.school.dto.request

import javax.validation.constraints.NotBlank

data class UpdateQuestionWebRequest(

    @field:NotBlank
    val question: String?,

    @field:NotBlank
    val answer: String?

)
