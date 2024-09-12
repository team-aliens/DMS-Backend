package team.aliens.dms.domain.volunteer.dto.request

import java.util.UUID

data class UpdateVolunteerRequest(
    val name: String,
    val content: String,
    val sexCondition: String,
    val gradeCondition: String,
    val score: Int,
    val optionalScore: Int,
    val maxApplicants: Int,
    val volunteerId: UUID
)
