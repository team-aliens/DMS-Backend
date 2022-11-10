package team.aliens.dms.manager.dto.request

import team.aliens.dms.domain.manager.dto.Sort
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

data class GetStudentListWebRequest(

    @field:NotBlank
    val name: String,

    @field:NotNull
    val sort: Sort
    
)