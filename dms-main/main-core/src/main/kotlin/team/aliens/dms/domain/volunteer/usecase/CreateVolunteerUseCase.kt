package team.aliens.dms.domain.volunteer.usecase

import team.aliens.dms.common.annotation.UseCase
import team.aliens.dms.common.service.security.SecurityService
import team.aliens.dms.domain.volunteer.dto.request.CreateVolunteerRequest
import team.aliens.dms.domain.volunteer.model.Volunteer
import team.aliens.dms.domain.volunteer.service.VolunteerService

@UseCase
class CreateVolunteerUseCase(
    private val securityService: SecurityService,
    private val volunteerService: VolunteerService
) {

    fun execute(createVolunteerRequest: CreateVolunteerRequest) {
        val schoolId = securityService.getCurrentSchoolId()

        val volunteer = Volunteer(
            name = createVolunteerRequest.name,
            availableSex = createVolunteerRequest.availableSex,
            availableGrade = createVolunteerRequest.availableGrade,
            score = createVolunteerRequest.score,
            optionalScore = createVolunteerRequest.optionalScore,
            maxApplicants = createVolunteerRequest.maxApplicants,
            schoolId = schoolId
        )

        volunteerService.saveVolunteer(volunteer)
    }
}
