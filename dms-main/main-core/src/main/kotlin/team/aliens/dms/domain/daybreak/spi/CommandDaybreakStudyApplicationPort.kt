package team.aliens.dms.domain.daybreak.spi

import team.aliens.dms.domain.daybreak.model.DaybreakStudyApplication
import java.util.UUID

interface CommandDaybreakStudyApplicationPort {

    fun saveDaybreakStudyApplication(application: DaybreakStudyApplication)

    fun saveAllDaybreakStudyApplications(applications: List<DaybreakStudyApplication>)

    fun deleteOutdatedDaybreakStudyApplications()

    // 삭제 성공 여부를 반환한다
    fun deleteDaybreakStudyApplication(studentId: UUID): Boolean
}
