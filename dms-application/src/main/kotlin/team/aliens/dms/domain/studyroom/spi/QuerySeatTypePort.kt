package team.aliens.dms.domain.studyroom.spi

import team.aliens.dms.domain.studyroom.model.SeatType
import java.util.UUID

interface QuerySeatTypePort {

    fun queryAllSeatTypeBySchoolId(schoolId: UUID): List<SeatType>

    fun existsSeatTypeByName(name: String): Boolean

    fun querySeatTypeById(seatTypeId: UUID): SeatType?
}
