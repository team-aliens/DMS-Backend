package team.aliens.dms.domain.student.dto.request

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class UpdateStudentProfileWebRequest(

    @field:NotBlank
    @field:Size(max = 500)
    val profileImageUrl: String

)
