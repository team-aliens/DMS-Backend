package team.aliens.dms.domain.volunteer.model

import team.aliens.dms.common.annotation.Aggregate
import team.aliens.dms.domain.volunteer.exception.VolunteerApplicationAlreadyAssigned
import java.util.UUID

@Aggregate
data class VolunteerApplication(

    val id: UUID = UUID(0, 0),

    val studentId: UUID,

    val volunteerId: UUID,

    val approved: Boolean,
) {
    fun checkCancelable(approved: Boolean) {
        if (approved) {
            throw VolunteerApplicationAlreadyAssigned
        }
    }
}
