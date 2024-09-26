package team.aliens.dms.domain.volunteer.service

import team.aliens.dms.common.annotation.Service
import team.aliens.dms.domain.volunteer.exception.VolunteerApplicationAlreadyApplied
import team.aliens.dms.domain.volunteer.spi.QueryVolunteerApplicationPort
import java.util.UUID

@Service
class CheckVolunteerServiceImpl(
    private val queryVolunteerApplicationPort: QueryVolunteerApplicationPort
): CheckVolunteerService {

    override fun checkVolunteerApplicationExists(studentId: UUID, volunteerId: UUID) {
        queryVolunteerApplicationPort
            .queryVolunteerApplicationByStudentIdAndVolunteerId(studentId, volunteerId)
            ?.let {
                throw VolunteerApplicationAlreadyApplied
            }
    }
}
