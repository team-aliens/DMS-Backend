package team.aliens.dms.domain.school.dto.request

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class UpdateQuestionWebRequest(

    @field:NotBlank
    @field:Size(max = 100)
    val question: String,

    @field:NotBlank
    @field:Size(max = 100)
    val answer: String

)
