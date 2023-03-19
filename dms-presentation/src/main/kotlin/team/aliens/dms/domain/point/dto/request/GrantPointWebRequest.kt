package team.aliens.dms.domain.point.dto.request

import java.util.UUID
import javax.validation.ConstraintViolationException
import javax.validation.constraints.NotNull

data class GrantPointWebRequest(
    @field:NotNull
    val pointOptionId: UUID?,

    @field:NotNull
    val studentIdList: List<UUID>?
) {
    init {
        studentIdList?.forEach {
            it as UUID? ?: throw ConstraintViolationException(setOf())
        }
    }
}
