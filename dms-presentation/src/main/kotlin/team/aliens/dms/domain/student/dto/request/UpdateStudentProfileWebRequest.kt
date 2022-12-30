package team.aliens.dms.domain.student.dto.request

import javax.validation.constraints.NotBlank
import javax.validation.constraints.Size

data class UpdateStudentProfileWebRequest(

    @field:NotBlank
    @field:Size(max = 500)
    val profileImageUrl: String?

)
