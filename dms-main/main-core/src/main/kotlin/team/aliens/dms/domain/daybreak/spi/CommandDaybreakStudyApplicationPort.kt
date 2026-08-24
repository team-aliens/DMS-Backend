package team.aliens.dms.domain.daybreak.spi

import team.aliens.dms.domain.daybreak.model.DaybreakStudyApplication
import java.util.UUID

interface CommandDaybreakStudyApplicationPort {

    fun saveDaybreakStudyApplication(application: DaybreakStudyApplication)

    fun saveAllDaybreakStudyApplications(applications: List<DaybreakStudyApplication>)

    fun deleteOutdatedDaybreakStudyApplications()

    fun deleteDaybreakStudyApplication(studentId: UUID)
}
