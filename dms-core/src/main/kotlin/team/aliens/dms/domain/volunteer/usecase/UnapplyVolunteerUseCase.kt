package team.aliens.dms.domain.volunteer.usecase

import team.aliens.dms.common.annotation.UseCase
import team.aliens.dms.domain.volunteer.service.VolunteerService
import java.util.UUID

@UseCase
class UnapplyVolunteerUseCase(
    private val volunteerService: VolunteerService
) {

    fun execute(volunteerApplicationId: UUID) {
        val volunteer = volunteerService.getVolunteerApplicationById(volunteerApplicationId)

        volunteer.checkIsNotApproved()

        volunteerService.deleteVolunteerApplication(volunteer)
    }
}
