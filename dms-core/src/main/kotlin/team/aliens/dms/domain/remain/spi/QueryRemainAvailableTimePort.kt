package team.aliens.dms.domain.remain.spi

import team.aliens.dms.domain.remain.model.RemainAvailableTime
import java.util.UUID

interface QueryRemainAvailableTimePort {

    fun queryRemainAvailableTimeBySchoolId(schoolId: UUID): RemainAvailableTime?
}
