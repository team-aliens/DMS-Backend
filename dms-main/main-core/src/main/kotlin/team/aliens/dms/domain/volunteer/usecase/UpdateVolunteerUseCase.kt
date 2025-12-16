package team.aliens.dms.domain.volunteer.usecase

import team.aliens.dms.common.annotation.UseCase
import team.aliens.dms.domain.volunteer.dto.request.UpdateVolunteerRequest
import team.aliens.dms.domain.volunteer.service.VolunteerService

@UseCase
class UpdateVolunteerUseCase(
    private val volunteerService: VolunteerService
) {

    fun execute(updateVolunteerRequest: UpdateVolunteerRequest) {
        val currentVolunteer = volunteerService.getVolunteerById(updateVolunteerRequest.volunteerId)

        volunteerService.saveVolunteer(
            currentVolunteer.copy(
                name = updateVolunteerRequest.name,
                availableSex = updateVolunteerRequest.availableSex,
                availableGrade = updateVolunteerRequest.availableGrade,
                maxScore = updateVolunteerRequest.maxScore,
                minScore = updateVolunteerRequest.minScore,
                maxApplicants = updateVolunteerRequest.maxApplicants,
            )
        )
    }
}
