package team.aliens.dms.domain.student.dto.request

import com.fasterxml.jackson.annotation.JsonProperty
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

data class FindStudentAccountIdWebRequest(

    @field:NotBlank
    val name: String,

    @field:NotNull
    val grade: Int,

    @field:NotNull
    @field:JsonProperty("class_room")
    val classRoom: Int,

    @field:NotNull
    val number: Int

)
