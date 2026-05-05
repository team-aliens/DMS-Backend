package team.aliens.dms.domain.daybreak.service

import team.aliens.dms.domain.daybreak.model.DaybreakStudyApplication
import team.aliens.dms.domain.daybreak.model.DaybreakStudyType

interface CommandDaybreakService {

    fun saveDaybreakStudyApplication(application: DaybreakStudyApplication)

    fun saveDaybreakStudyType(type: DaybreakStudyType)

    fun saveAllDaybreakStudyApplications(applications: List<DaybreakStudyApplication>)
}
