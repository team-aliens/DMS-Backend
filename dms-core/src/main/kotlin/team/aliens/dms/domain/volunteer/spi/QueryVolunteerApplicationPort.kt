package team.aliens.dms.domain.volunteer.spi

import team.aliens.dms.domain.volunteer.model.VolunteerApplication
import team.aliens.dms.domain.volunteer.spi.vo.VolunteerApplicantVO
import java.util.UUID

interface QueryVolunteerApplicationPort {

    fun queryVolunteerApplicationById(volunteerApplicationId: UUID): VolunteerApplication?

    fun queryVolunteerApplicationsByStudentId(studentId: UUID): List<VolunteerApplication>

    fun queryAllApplicantsByVolunteerId(volunteerId: UUID): List<VolunteerApplicantVO>
}
