package team.aliens.dms.domain.volunteer.stub

import team.aliens.dms.domain.volunteer.service.CheckVolunteerService
import java.util.UUID

abstract class CheckVolunteerServiceStub : CheckVolunteerService {
    override fun checkVolunteerApplicationExists(studentId: UUID, volunteerId: UUID) =
        throw UnsupportedOperationException()
}
