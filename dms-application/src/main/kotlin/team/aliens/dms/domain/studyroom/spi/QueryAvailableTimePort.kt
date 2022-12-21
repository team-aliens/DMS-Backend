package team.aliens.dms.domain.studyroom.spi

import java.util.UUID
import team.aliens.dms.domain.studyroom.model.AvailableTime

interface QueryAvailableTimePort {

    fun queryAvailableTimeBySchoolId(schoolId: UUID): AvailableTime?

}