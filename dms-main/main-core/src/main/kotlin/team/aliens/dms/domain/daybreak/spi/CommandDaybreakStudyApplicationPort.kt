package team.aliens.dms.domain.daybreak.spi

import team.aliens.dms.domain.daybreak.model.DaybreakStudyApplication
import team.aliens.dms.domain.daybreak.model.Status

interface CommandDaybreakStudyApplicationPort {

    fun saveDaybreakStudyApplication(application: DaybreakStudyApplication)

    fun changeStatusDaybreakStudyApplication(status: Status)
}
