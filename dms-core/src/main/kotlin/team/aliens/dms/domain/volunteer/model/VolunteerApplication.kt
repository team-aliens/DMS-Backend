package team.aliens.dms.domain.volunteer.model

import team.aliens.dms.common.annotation.Aggregate
import team.aliens.dms.domain.volunteer.exception.VolunteerApplicationAlreadyAssigned
import team.aliens.dms.domain.volunteer.exception.VolunteerApplicationNotAssigned
import java.util.UUID

@Aggregate
data class VolunteerApplication(

    val id: UUID = UUID(0, 0),

    val studentId: UUID,

    val volunteerId: UUID,

    val approved: Boolean,
) {
    fun checkIsNotApproved() {
        if (approved) {
            throw VolunteerApplicationAlreadyAssigned
        }
    }

    fun checkIsApproved() {
        if (!approved) {
            throw VolunteerApplicationNotAssigned
        }
    }
}
