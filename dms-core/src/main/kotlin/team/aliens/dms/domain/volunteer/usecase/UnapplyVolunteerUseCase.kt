package team.aliens.dms.domain.volunteer.usecase

import team.aliens.dms.common.annotation.UseCase
import team.aliens.dms.domain.student.service.StudentService
import team.aliens.dms.domain.volunteer.service.VolunteerService
import java.util.UUID

@UseCase
class UnapplyVolunteerUseCase(
    private val volunteerService: VolunteerService,
    private val studentService: StudentService
) {

    fun execute(volunteerId: UUID) {
        val student = studentService.getCurrentStudent()

        val volunteer = volunteerService.getVolunteerApplicationByVolunteerIdAndStudentId(
            volunteerId = volunteerId,
            studentId = student.id
        )

        volunteer.checkIsNotApproved()

        volunteerService.deleteVolunteerApplication(volunteer)
    }
}
