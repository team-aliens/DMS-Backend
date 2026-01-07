package team.aliens.dms.domain.volunteer.usecase

import team.aliens.dms.common.annotation.ReadOnlyUseCase
import team.aliens.dms.domain.volunteer.dto.response.VolunteerAssignScoreResponse
import team.aliens.dms.domain.volunteer.service.VolunteerService
import java.util.UUID

@ReadOnlyUseCase
class QueryAssignScoreUseCase(
    private val volunteerService: VolunteerService
) {
    fun execute(applicationId: UUID): VolunteerAssignScoreResponse {
        val volunteerScore = volunteerService.getVolunteerApplicationScoreById(applicationId)

        return VolunteerAssignScoreResponse(
            assignedScore = volunteerScore.assignScore
        )
    }
}
