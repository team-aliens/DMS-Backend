package team.aliens.dms.domain.student.dto.request

import java.util.UUID
import javax.validation.constraints.NotNull

data class CheckStudentGcnWebRequest(

    @field:NotNull
    val schoolId: UUID,

    @field:NotNull
    val grade: Int,

    @field:NotNull
    val classRoom: Int,

    @field:NotNull
    val number: Int

)