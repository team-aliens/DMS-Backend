package team.aliens.dms.domain.volunteer.usecase

import team.aliens.dms.common.annotation.UseCase
import team.aliens.dms.domain.student.service.StudentService
import team.aliens.dms.domain.volunteer.dto.response.ApplyVolunteerResponse
import team.aliens.dms.domain.volunteer.model.VolunteerApplication
import team.aliens.dms.domain.volunteer.service.VolunteerService
import java.util.UUID

@UseCase
class ApplyVolunteerUseCase(
    private val volunteerService: VolunteerService,
    private val studentService: StudentService,
) {

    fun execute(volunteerId: UUID): ApplyVolunteerResponse {
        val student = studentService.getCurrentStudent()

        val volunteer = volunteerService.saveVolunteerApplication(
            VolunteerApplication(
                studentId = student.id,
                volunteerId = volunteerId,
                approved = false
            )
        )

        return ApplyVolunteerResponse(volunteer.id)
    }
}
