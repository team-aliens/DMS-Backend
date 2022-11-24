package team.aliens.dms.domain.student.dto.request

import com.fasterxml.jackson.annotation.JsonProperty
import java.util.UUID
import javax.validation.constraints.NotNull

data class CheckStudentGcnWebRequest(

    @field:NotNull
    @field:JsonProperty("school_id")
    val schoolId: UUID,

    @field:NotNull
    val grade: Int,

    @field:NotNull
    @field:JsonProperty("class_room")
    val classRoom: Int,

    @field:NotNull
    val number: Int

)