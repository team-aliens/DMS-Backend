package team.aliens.dms.domain.studyroom.spi

import java.util.UUID
import team.aliens.dms.domain.studyroom.model.SeatType

interface QuerySeatTypePort {

    fun queryAllSeatTypeBySchoolId(schoolId: UUID): List<SeatType>

    fun existsSeatTypeByName(name: String): Boolean

    fun querySeatTypeId(seatId: UUID?): SeatType?

}