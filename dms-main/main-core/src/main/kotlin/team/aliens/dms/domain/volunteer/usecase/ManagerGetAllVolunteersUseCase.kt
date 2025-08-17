package team.aliens.dms.domain.volunteer.usecase

import team.aliens.dms.common.annotation.ReadOnlyUseCase
import team.aliens.dms.common.service.security.SecurityService
import team.aliens.dms.domain.volunteer.dto.response.VolunteerResponse
import team.aliens.dms.domain.volunteer.dto.response.VolunteersResponse
import team.aliens.dms.domain.volunteer.service.VolunteerService

@ReadOnlyUseCase
class ManagerGetAllVolunteersUseCase(
    private val securityService: SecurityService,
    private val volunteerService: VolunteerService
) {

    fun execute(): VolunteersResponse {
        val schoolId = securityService.getCurrentSchoolId()

        val volunteers = volunteerService.getAllVolunteersWithCurrentApplicantsBySchoolId(schoolId)
            .map { VolunteerResponse.of(it) }

        return VolunteersResponse(volunteers)
    }
}
