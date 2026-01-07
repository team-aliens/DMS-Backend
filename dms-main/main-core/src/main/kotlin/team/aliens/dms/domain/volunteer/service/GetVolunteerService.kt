package team.aliens.dms.domain.volunteer.service

import team.aliens.dms.domain.student.model.Student
import team.aliens.dms.domain.volunteer.model.Volunteer
import team.aliens.dms.domain.volunteer.model.VolunteerApplication
import team.aliens.dms.domain.volunteer.model.VolunteerApplicationStatus
import team.aliens.dms.domain.volunteer.model.VolunteerScore
import team.aliens.dms.domain.volunteer.spi.vo.CurrentVolunteerApplicantVO
import team.aliens.dms.domain.volunteer.spi.vo.VolunteerApplicantVO
import team.aliens.dms.domain.volunteer.spi.vo.VolunteerScoreWithStudentVO
import team.aliens.dms.domain.volunteer.spi.vo.VolunteerWithCurrentApplicantVO
import java.util.UUID

interface GetVolunteerService {

    fun getVolunteerApplicationById(volunteerApplicationId: UUID): VolunteerApplication

    fun getVolunteerApplicationByVolunteerIdAndStudentId(volunteerId: UUID, studentId: UUID): VolunteerApplication

    fun getVolunteerById(volunteerId: UUID): Volunteer

    fun getAllVolunteersWithCurrentApplicantsByStudent(student: Student): List<VolunteerWithCurrentApplicantVO>

    fun getAllVolunteersWithCurrentApplicantsBySchoolId(schoolId: UUID): List<VolunteerWithCurrentApplicantVO>

    fun getAllApplicantsByVolunteerId(volunteerId: UUID): List<VolunteerApplicantVO>

    fun getAllApplicantsBySchoolIdGroupByVolunteer(schoolId: UUID): List<CurrentVolunteerApplicantVO>

    fun getVolunteerApplicationsWithVolunteersByStudentId(studentId: UUID): List<Triple<VolunteerApplication, Volunteer, VolunteerApplicationStatus>>

    fun getVolunteerApplicationScoreById(applicationId: UUID): VolunteerScore

    fun getAllVolunteerScoresWithVO(): List<VolunteerScoreWithStudentVO>
}
