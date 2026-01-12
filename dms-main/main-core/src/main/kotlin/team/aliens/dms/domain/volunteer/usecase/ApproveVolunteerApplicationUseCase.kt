package team.aliens.dms.domain.volunteer.usecase

import team.aliens.dms.common.annotation.UseCase
import team.aliens.dms.domain.volunteer.service.VolunteerService
import java.util.UUID

@UseCase
class ApproveVolunteerApplicationUseCase(
    private val volunteerService: VolunteerService
) {

    fun execute(volunteerApplicationId: UUID) {

        val currentVolunteerApplication = volunteerService.getVolunteerApplicationById(volunteerApplicationId)

        currentVolunteerApplication.checkIsNotApproved()

        val approvedApplication = volunteerService.saveVolunteerApplication(
            currentVolunteerApplication.copy(
                approved = true
            )
        )

        val volunteer = volunteerService.getVolunteerById(approvedApplication.volunteerId)

        volunteerService.createVolunteerScore(approvedApplication, volunteer)
    }
}
