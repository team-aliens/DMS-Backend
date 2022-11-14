package team.aliens.dms.student.dto.request

import javax.validation.constraints.NotBlank

data class UpdateStudentProfileWebRequest(

    @field:NotBlank
    val profileImageUrl: String

)
