package team.aliens.dms.domain.volunteer.dto.request

import team.aliens.dms.domain.student.model.Sex
import team.aliens.dms.domain.volunteer.model.AvailableGrade
import java.util.UUID

data class UpdateVolunteerRequest(
    val name: String,
    val availableSex: Sex,
    val availableGrade: AvailableGrade,
    val maxScore: Int,
    val minScore: Int,
    val maxApplicants: Int,
    val volunteerId: UUID
)
