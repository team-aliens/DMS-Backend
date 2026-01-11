package team.aliens.dms.domain.volunteer.stub

import team.aliens.dms.domain.student.model.Student
import team.aliens.dms.domain.volunteer.model.Volunteer
import team.aliens.dms.domain.volunteer.model.VolunteerApplication
import team.aliens.dms.domain.volunteer.model.VolunteerApplicationStatus
import team.aliens.dms.domain.volunteer.service.GetVolunteerService
import team.aliens.dms.domain.volunteer.spi.vo.CurrentVolunteerApplicantVO
import team.aliens.dms.domain.volunteer.spi.vo.VolunteerApplicantVO
import team.aliens.dms.domain.volunteer.spi.vo.VolunteerScoreWithStudentVO
import team.aliens.dms.domain.volunteer.spi.vo.VolunteerWithCurrentApplicantVO
import java.util.UUID

abstract class GetVolunteerServiceStub : GetVolunteerService {
    override fun getVolunteerApplicationById(volunteerApplicationId: UUID): VolunteerApplication =
        throw UnsupportedOperationException()
    override fun getVolunteerApplicationByVolunteerIdAndStudentId(
        volunteerId: UUID,
        studentId: UUID
    ): VolunteerApplication =
        throw UnsupportedOperationException()
    override fun getVolunteerById(volunteerId: UUID): Volunteer =
        throw UnsupportedOperationException()
    override fun getAllVolunteersWithCurrentApplicantsByStudent(
        student: Student
    ): List<VolunteerWithCurrentApplicantVO> =
        throw UnsupportedOperationException()
    override fun getAllVolunteersWithCurrentApplicantsBySchoolId(
        schoolId: UUID
    ): List<VolunteerWithCurrentApplicantVO> =
        throw UnsupportedOperationException()
    override fun getAllApplicantsByVolunteerId(volunteerId: UUID): List<VolunteerApplicantVO> =
        throw UnsupportedOperationException()
    override fun getAllApplicantsBySchoolIdGroupByVolunteer(schoolId: UUID): List<CurrentVolunteerApplicantVO> =
        throw UnsupportedOperationException()
    override fun getVolunteerApplicationsWithVolunteersByStudentId(
        studentId: UUID
    ): List<Triple<VolunteerApplication, Volunteer, VolunteerApplicationStatus>> =
        throw UnsupportedOperationException()
    override fun getAllVolunteerScoresWithVO(): List<VolunteerScoreWithStudentVO> =
        throw UnsupportedOperationException()
}
