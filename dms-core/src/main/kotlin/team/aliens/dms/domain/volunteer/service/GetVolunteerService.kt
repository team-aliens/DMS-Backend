package team.aliens.dms.domain.volunteer.service

import team.aliens.dms.domain.volunteer.model.Volunteer
import team.aliens.dms.domain.volunteer.model.VolunteerApplication
import team.aliens.dms.domain.volunteer.spi.vo.VolunteerApplicantVO
import java.util.UUID

interface GetVolunteerService {

    fun getVolunteerApplicationById(volunteerApplicationId: UUID): VolunteerApplication

    fun getVolunteerById(volunteerId: UUID): Volunteer

    fun getVolunteerByCondition(studentId: UUID): List<Volunteer>

    fun getVolunteerApplicationsByStudentId(studentId: UUID): List<VolunteerApplication>

    fun getAllVolunteersBySchoolId(schoolId: UUID): List<Volunteer>

    fun getAllApplicantsByVolunteerId(volunteerId: UUID): List<VolunteerApplicantVO>
}
