package team.aliens.dms.domain.volunteer.usecase

import team.aliens.dms.common.annotation.UseCase
import team.aliens.dms.domain.volunteer.service.VolunteerService
import java.util.UUID

@UseCase
class RejectVolunteerApplicationUseCase(
    private val volunteerService: VolunteerService
) {

    fun execute(volunteerApplicationId: UUID) {
        val currentVolunteerApplication = volunteerService.getVolunteerApplicationById(volunteerApplicationId)

        volunteerService.deleteVolunteerApplication(currentVolunteerApplication)
    }
}
