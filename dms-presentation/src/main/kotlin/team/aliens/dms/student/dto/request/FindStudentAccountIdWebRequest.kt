package team.aliens.dms.student.dto.request

import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

data class FindStudentAccountIdWebRequest(

    @field:NotBlank
    val name: String,

    @field:NotNull
    val grade: Int,

    @field:NotNull
    val classRoom: Int,

    @field:NotNull
    val number: Int,
)
