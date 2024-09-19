package team.aliens.dms.domain.volunteer.dto.request

import java.util.UUID

data class UpdateVolunteerRequest(
    val name: String,
    val content: String,
    val availableSex: String,
    val availableGrade: String,
    val score: Int,
    val optionalScore: Int,
    val maxApplicants: Int,
    val volunteerId: UUID
)
