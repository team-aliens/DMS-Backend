package team.aliens.dms.domain.daybreak.service

import team.aliens.dms.domain.daybreak.model.DaybreakStudyApplication
import team.aliens.dms.domain.daybreak.model.DaybreakStudyType
import team.aliens.dms.domain.daybreak.model.Status

interface CommandDaybreakService {

    fun saveDaybreakStudyApplication(application: DaybreakStudyApplication)

    fun changeStatusDaybreakStudyApplication(status: Status)

    fun saveDaybreakStudyType(type: DaybreakStudyType)
}
