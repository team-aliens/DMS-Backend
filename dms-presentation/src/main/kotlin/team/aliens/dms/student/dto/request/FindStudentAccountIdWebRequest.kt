package team.aliens.dms.student.dto.request

import javax.validation.constraints.NotBlank

data class FindStudentAccountIdWebRequest(

    @field:NotBlank
    val name: String,

    val grade: Int,

    val classRoom: Int,

    val number: Int,
)
