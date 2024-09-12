package team.aliens.dms.domain.volunteer.spi

import team.aliens.dms.domain.volunteer.model.VolunteerApplication
import java.util.UUID

interface QueryVolunteerApplicationPort {

    fun queryVolunteerApplicationById(volunteerApplicationId: UUID): VolunteerApplication?

    fun queryVolunteerApplicationsByStudentId(studentId: UUID): List<VolunteerApplication>
}
