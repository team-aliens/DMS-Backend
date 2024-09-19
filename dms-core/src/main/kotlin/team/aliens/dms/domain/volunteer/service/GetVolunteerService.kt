package team.aliens.dms.domain.volunteer.service

import team.aliens.dms.domain.volunteer.model.Volunteer
import team.aliens.dms.domain.volunteer.model.VolunteerApplication
import team.aliens.dms.domain.volunteer.spi.vo.CurrentVolunteerApplicantVO
import team.aliens.dms.domain.volunteer.spi.vo.VolunteerApplicantVO
import java.util.UUID

interface GetVolunteerService {

    fun getVolunteerApplicationById(volunteerApplicationId: UUID): VolunteerApplication

    fun getVolunteerById(volunteerId: UUID): Volunteer

    fun getVolunteerByStudentId(studentId: UUID): List<Volunteer>

    fun getAllVolunteersBySchoolId(schoolId: UUID): List<Volunteer>

    fun getAllApplicantsByVolunteerId(volunteerId: UUID): List<VolunteerApplicantVO>

    fun getAllApplicantsBySchoolIdGroupByVolunteer(schoolId: UUID): List<CurrentVolunteerApplicantVO>

    fun getAllVolunteers(): List<Volunteer>

    fun getVolunteerApplicationsWithVolunteersByStudentId(studentId: UUID): List<Pair<VolunteerApplication, Volunteer>>
}
