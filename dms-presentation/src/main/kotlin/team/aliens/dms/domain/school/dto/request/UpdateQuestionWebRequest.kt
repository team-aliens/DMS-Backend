package team.aliens.dms.domain.school.dto.request

import javax.validation.constraints.NotBlank
import javax.validation.constraints.Size

data class UpdateQuestionWebRequest(

    @field:NotBlank
    @field:Size(max = 100)
    val question: String,

    @field:NotBlank
    @field:Size(max = 100)
    val answer: String

)
