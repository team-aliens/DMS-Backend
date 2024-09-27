package team.aliens.dms.domain.volunteer.service

import team.aliens.dms.common.annotation.Service
import team.aliens.dms.domain.student.model.Student
import team.aliens.dms.domain.volunteer.exception.VolunteerApplicationNotFoundException
import team.aliens.dms.domain.volunteer.exception.VolunteerNotFoundException
import team.aliens.dms.domain.volunteer.model.Volunteer
import team.aliens.dms.domain.volunteer.model.VolunteerApplication
import team.aliens.dms.domain.volunteer.spi.QueryVolunteerApplicationPort
import team.aliens.dms.domain.volunteer.spi.QueryVolunteerPort
import team.aliens.dms.domain.volunteer.spi.vo.CurrentVolunteerApplicantVO
import team.aliens.dms.domain.volunteer.spi.vo.VolunteerApplicantVO
import team.aliens.dms.domain.volunteer.spi.vo.VolunteerWithCurrentApplicantVO
import java.util.UUID

@Service
class GetVolunteerServiceImpl(
    private val queryVolunteerApplicationPort: QueryVolunteerApplicationPort,
    private val queryVolunteerPort: QueryVolunteerPort
) : GetVolunteerService {

    override fun getVolunteerApplicationById(volunteerApplicationId: UUID): VolunteerApplication =
        queryVolunteerApplicationPort.queryVolunteerApplicationById(volunteerApplicationId)
            ?: throw VolunteerApplicationNotFoundException

    override fun getVolunteerById(volunteerId: UUID): Volunteer =
        queryVolunteerPort.queryVolunteerById(volunteerId)
            ?: throw VolunteerNotFoundException

    override fun getAllVolunteersWithCurrentApplicantsByStudent(student: Student): List<VolunteerWithCurrentApplicantVO> {
        val volunteers = queryVolunteerPort.queryAllVolunteersWithCurrentApplicantsBySchoolId(student.schoolId)

        return volunteers.filter { volunteer ->
            volunteer.toVolunteer().isAvailable(student)
        }
    }

    override fun getAllVolunteersWithCurrentApplicantsBySchoolId(schoolId: UUID): List<VolunteerWithCurrentApplicantVO> =
        queryVolunteerPort.queryAllVolunteersWithCurrentApplicantsBySchoolId(schoolId)

    override fun getAllApplicantsByVolunteerId(volunteerId: UUID): List<VolunteerApplicantVO> =
        queryVolunteerApplicationPort.queryAllApplicantsByVolunteerId(volunteerId)

    override fun getAllApplicantsBySchoolIdGroupByVolunteer(schoolId: UUID): List<CurrentVolunteerApplicantVO> =
        queryVolunteerApplicationPort.queryAllApplicantsBySchoolIdGroupByVolunteer(schoolId)

    override fun getVolunteerApplicationsWithVolunteersByStudentId(studentId: UUID): List<Pair<VolunteerApplication, Volunteer>> {
        return queryVolunteerApplicationPort.getVolunteerApplicationsWithVolunteersByStudentId(studentId)
    }
}
