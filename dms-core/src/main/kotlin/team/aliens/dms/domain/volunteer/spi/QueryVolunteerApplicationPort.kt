package team.aliens.dms.domain.volunteer.spi

import team.aliens.dms.domain.volunteer.model.Volunteer
import team.aliens.dms.domain.volunteer.model.VolunteerApplication
import team.aliens.dms.domain.volunteer.spi.vo.CurrentVolunteerApplicantVO
import team.aliens.dms.domain.volunteer.spi.vo.VolunteerApplicantVO
import java.util.UUID

interface QueryVolunteerApplicationPort {

    fun queryVolunteerApplicationById(volunteerApplicationId: UUID): VolunteerApplication?

    fun queryAllApplicantsByVolunteerId(volunteerId: UUID): List<VolunteerApplicantVO>

    fun queryAllApplicantsBySchoolIdGroupByVolunteer(schoolId: UUID): List<CurrentVolunteerApplicantVO>

    fun queryVolunteerApplicationByStudentIdAndVolunteerId(studentId: UUID, volunteerId: UUID): VolunteerApplication?

    fun getVolunteerApplicationsWithVolunteersByStudentId(studentId: UUID): List<Pair<VolunteerApplication, Volunteer>>
}
