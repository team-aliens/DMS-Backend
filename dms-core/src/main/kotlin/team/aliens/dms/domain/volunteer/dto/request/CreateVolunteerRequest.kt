package team.aliens.dms.domain.volunteer.dto.request

import team.aliens.dms.domain.student.model.Sex
import team.aliens.dms.domain.volunteer.model.AvailableGrade

data class CreateVolunteerRequest(
    val name: String,
    val availableSex: Sex,
    val availableGrade: AvailableGrade,
    val score: Int,
    val optionalScore: Int,
    val maxApplicants: Int
)
