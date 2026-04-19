package team.aliens.dms.domain.daybreak.spi

import team.aliens.dms.domain.daybreak.model.DaybreakStudyType
import java.util.UUID

interface QueryDaybreakStudyTypePort {
    fun daybreakStudyTypeById(id: UUID): DaybreakStudyType?

    fun daybreakStudyTypesBySchoolId(id: UUID): List<DaybreakStudyType>

    fun existsDaybreakStudyTypeBySchoolIdAndName(schoolId: UUID, name: String): Boolean
}
