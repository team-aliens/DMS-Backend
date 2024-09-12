package team.aliens.dms.domain.volunteer.usecase

import team.aliens.dms.common.annotation.UseCase
import team.aliens.dms.common.service.security.SecurityService
import team.aliens.dms.domain.student.model.Sex
import team.aliens.dms.domain.volunteer.dto.request.CreateVolunteerRequest
import team.aliens.dms.domain.volunteer.model.GradeCondition
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
            content = createVolunteerRequest.content,
            sexCondition = Sex.valueOf(createVolunteerRequest.sexCondition),
            gradeCondition = GradeCondition.valueOf(createVolunteerRequest.gradeCondition),
            score = createVolunteerRequest.score,
            optionalScore = createVolunteerRequest.optionalScore,
            maxApplicants = createVolunteerRequest.maxApplicants,
            schoolId = schoolId
        )

        volunteerService.saveVolunteer(volunteer)
    }
}
