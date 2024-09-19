package team.aliens.dms.domain.volunteer.usecase

import team.aliens.dms.common.annotation.UseCase
import team.aliens.dms.domain.student.model.Sex
import team.aliens.dms.domain.volunteer.dto.request.UpdateVolunteerRequest
import team.aliens.dms.domain.volunteer.model.GradeCondition
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
                content = updateVolunteerRequest.content,
                availableSex = Sex.valueOf(updateVolunteerRequest.availableSex),
                availableGrade = GradeCondition.valueOf(updateVolunteerRequest.availableGrade),
                score = updateVolunteerRequest.score,
                optionalScore = updateVolunteerRequest.optionalScore,
                maxApplicants = updateVolunteerRequest.maxApplicants,
            )
        )
    }
}
