package team.aliens.dms.domain.volunteer.usecase

import team.aliens.dms.common.annotation.UseCase
import team.aliens.dms.domain.volunteer.service.VolunteerService
import java.util.UUID

@UseCase
class UpdateVolunteerScoreUseCase(
    private val volunteerService: VolunteerService
) {

    fun execute(applicationId: UUID, selectedScore: Int) {
        volunteerService.updateVolunteerScore(applicationId, selectedScore)
    }
}
