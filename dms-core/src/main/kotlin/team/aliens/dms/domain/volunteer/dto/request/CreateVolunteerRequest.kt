package team.aliens.dms.domain.volunteer.dto.request

data class CreateVolunteerRequest(
    val name: String,
    val content: String,
    val sexCondition: String,
    val gradeCondition: String,
    val score: Int,
    val optionalScore: Int,
    val maxApplicants: Int
)
