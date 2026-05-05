package team.aliens.dms.domain.daybreak.spi

import team.aliens.dms.domain.daybreak.model.DaybreakStudyApplication

interface CommandDaybreakStudyApplicationPort {

    fun saveDaybreakStudyApplication(application: DaybreakStudyApplication)

    fun saveAllDaybreakStudyApplications(applications: List<DaybreakStudyApplication>)
}
