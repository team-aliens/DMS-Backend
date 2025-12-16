package team.aliens.dms.domain.volunteer.usecase

import team.aliens.dms.common.annotation.UseCase
import team.aliens.dms.domain.volunteer.service.VolunteerScoreService
import java.util.UUID

@UseCase
class UpdateVolunteerScoreUseCase(
    private val volunteerScoreService: VolunteerScoreService,
) {

    fun execute(applicationId: UUID, selectedScore: Int) {
        volunteerScoreService.updateVolunteerScore(applicationId, selectedScore)
    }
}
