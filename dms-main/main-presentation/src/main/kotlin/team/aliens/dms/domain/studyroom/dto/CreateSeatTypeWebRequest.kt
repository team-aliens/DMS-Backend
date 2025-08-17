package team.aliens.dms.domain.studyroom.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class CreateSeatTypeWebRequest(

    @field:NotBlank
    @field:Size(max = 20)
    val name: String,

    @field:Size(min = 7, max = 7, message = "색깔은 7글자 이여야 합니다")
    @field:NotBlank
    val color: String

)
