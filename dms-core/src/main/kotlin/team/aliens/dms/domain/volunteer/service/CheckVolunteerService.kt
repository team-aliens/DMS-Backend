package team.aliens.dms.domain.volunteer.service

import java.util.UUID

interface CheckVolunteerService {

    fun checkVolunteerApplicationExists(studentId: UUID, volunteerId: UUID)
}
