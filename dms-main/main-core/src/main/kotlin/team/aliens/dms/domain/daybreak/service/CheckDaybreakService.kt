package team.aliens.dms.domain.daybreak.service

import java.util.UUID

interface CheckDaybreakService {
    fun checkDaybreakStudyApplicationExists(studentId: UUID)

    fun checkDaybreakStudyTypeExists(schoolId: UUID, name: String)
}
