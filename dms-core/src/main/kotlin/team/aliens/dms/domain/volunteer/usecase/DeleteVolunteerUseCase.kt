package team.aliens.dms.domain.volunteer.usecase

import team.aliens.dms.common.annotation.UseCase
import team.aliens.dms.domain.volunteer.service.VolunteerService
import java.util.UUID

@UseCase
class DeleteVolunteerUseCase(
    private val volunteerService: VolunteerService
) {

    fun execute(volunteerId: UUID) {

        val currentVolunteer = volunteerService.getVolunteerById(volunteerId)

        volunteerService.deleteVolunteer(currentVolunteer)
    }
}
