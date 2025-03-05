package team.aliens.dms.domain.volunteer.spi

import team.aliens.dms.domain.volunteer.model.Volunteer
import team.aliens.dms.domain.volunteer.spi.vo.VolunteerWithCurrentApplicantVO
import java.util.UUID

interface QueryVolunteerPort {

    fun queryVolunteerById(volunteerId: UUID): Volunteer?

    fun queryAllVolunteersBySchoolId(schoolId: UUID): List<Volunteer>

    fun queryAllVolunteersWithCurrentApplicantsBySchoolIdAndStudentId(schoolId: UUID, studentId: UUID?): List<VolunteerWithCurrentApplicantVO>
}
