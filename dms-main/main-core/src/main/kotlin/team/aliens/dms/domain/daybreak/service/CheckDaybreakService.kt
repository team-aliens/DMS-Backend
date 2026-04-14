package team.aliens.dms.domain.daybreak.service

import java.util.UUID

interface CheckDaybreakService {
    fun checkDaybreakStudyApplicationByStudentId(studentId: UUID)
}
