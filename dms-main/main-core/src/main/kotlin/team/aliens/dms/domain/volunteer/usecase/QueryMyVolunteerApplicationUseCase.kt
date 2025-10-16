package team.aliens.dms.domain.volunteer.usecase

import team.aliens.dms.common.annotation.ReadOnlyUseCase
import team.aliens.dms.domain.student.service.StudentService
import team.aliens.dms.domain.volunteer.dto.response.QueryMyVolunteerApplicationResponse
import team.aliens.dms.domain.volunteer.service.VolunteerService

@ReadOnlyUseCase
class QueryMyVolunteerApplicationUseCase(
    private val volunteerService: VolunteerService,
    private val studentService: StudentService
) {
    fun execute(): QueryMyVolunteerApplicationResponse {
        val student = studentService.getCurrentStudent()

        val applicationsWithVolunteers = volunteerService.getVolunteerApplicationsWithVolunteersByStudentId(student.id)

        return QueryMyVolunteerApplicationResponse.of(applicationsWithVolunteers)
    }
}
