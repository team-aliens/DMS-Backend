package team.aliens.dms.domain.outing.model

import team.aliens.dms.common.annotation.Aggregate
import team.aliens.dms.common.model.SchoolIdDomain
import team.aliens.dms.domain.outing.exception.OutingTypeMismatchException
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.util.UUID

@Aggregate
data class OutingApplication(

    val id: UUID = UUID(0, 0),

    val studentId: UUID,

    val createdAt: LocalDateTime,

    val outAt: LocalDate,

    val outingTime: LocalTime,

    val arrivalTime: LocalTime,

    val status: OutingStatus,

    val reason: String,

    val destination: String,

    val outingTypeTitle: String,

    override val schoolId: UUID,

    val companionIds: List<UUID>? = null

) : SchoolIdDomain {

    fun checkCancelable(
        status: OutingStatus
    ) {
        if (status != OutingStatus.REQUESTED) {
            throw OutingTypeMismatchException
        }
    }
}
