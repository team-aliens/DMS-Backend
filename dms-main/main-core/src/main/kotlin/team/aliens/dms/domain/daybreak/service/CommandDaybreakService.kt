package team.aliens.dms.domain.daybreak.service

import team.aliens.dms.domain.daybreak.model.DaybreakStudyApplication
import team.aliens.dms.domain.daybreak.model.DaybreakStudyType
import java.util.UUID

interface CommandDaybreakService {

    fun saveDaybreakStudyApplication(application: DaybreakStudyApplication)

    fun saveDaybreakStudyType(type: DaybreakStudyType)

    fun saveAllDaybreakStudyApplications(applications: List<DaybreakStudyApplication>)

    fun deleteOutdatedDaybreakStudyApplications()

    // 학생이 자신의 신청을 삭제할 때 사용
    fun deleteDaybreakStudyApplication(studentId: UUID)
}
