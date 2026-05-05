package team.aliens.dms.domain.daybreak.spi

import team.aliens.dms.domain.daybreak.model.DaybreakStudyType

interface CommandDaybreakStudyTypePort {

    fun saveDaybreakStudyType(type: DaybreakStudyType)
}
