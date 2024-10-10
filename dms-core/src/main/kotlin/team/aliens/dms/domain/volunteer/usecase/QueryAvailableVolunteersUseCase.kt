package team.aliens.dms.domain.volunteer.usecase

import team.aliens.dms.common.annotation.ReadOnlyUseCase
import team.aliens.dms.domain.student.service.StudentService
import team.aliens.dms.domain.volunteer.dto.response.AvailableVolunteerResponse
import team.aliens.dms.domain.volunteer.dto.response.AvailableVolunteersResponse
import team.aliens.dms.domain.volunteer.service.VolunteerService

@ReadOnlyUseCase
class QueryAvailableVolunteersUseCase(
    private val volunteerService: VolunteerService,
    private val studentService: StudentService
) {

    fun execute(): AvailableVolunteersResponse {
        val student = studentService.getCurrentStudent()

        val availableVolunteers = volunteerService.getAllVolunteersWithCurrentApplicantsAndStatusByStudent(student)

        return AvailableVolunteersResponse(
            availableVolunteers
                .map { AvailableVolunteerResponse.of(it) }
        )
    }
}
