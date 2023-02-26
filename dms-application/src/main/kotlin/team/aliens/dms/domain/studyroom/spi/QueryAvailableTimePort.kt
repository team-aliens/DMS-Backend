package team.aliens.dms.domain.studyroom.spi

import team.aliens.dms.domain.studyroom.model.AvailableTime
import java.util.UUID

interface QueryAvailableTimePort {

    fun queryAvailableTimeBySchoolId(schoolId: UUID): AvailableTime?
}
