package team.aliens.dms.student.dto.request

import javax.validation.constraints.NotBlank

data class FindStudentAccountIdWebRequest(

    @field:NotBlank
    val name: String,

    @field:NotBlank
    val grade: Int,

    @field:NotBlank
    val classRoom: Int,

    @field:NotBlank
    val number: Int,
)
