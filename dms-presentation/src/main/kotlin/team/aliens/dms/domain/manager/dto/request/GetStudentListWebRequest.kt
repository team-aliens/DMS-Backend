package team.aliens.dms.domain.manager.dto.request

import team.aliens.dms.domain.manager.dto.Sort
import javax.validation.constraints.NotNull

data class GetStudentListWebRequest(

    val name: String?,

    @field:NotNull
    val sort: Sort?

)