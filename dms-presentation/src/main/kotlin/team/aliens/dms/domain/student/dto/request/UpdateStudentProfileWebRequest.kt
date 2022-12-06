package team.aliens.dms.domain.student.dto.request

import javax.validation.constraints.NotBlank

data class UpdateStudentProfileWebRequest(

    @field:NotBlank
    val profileImageUrl: String?

)
