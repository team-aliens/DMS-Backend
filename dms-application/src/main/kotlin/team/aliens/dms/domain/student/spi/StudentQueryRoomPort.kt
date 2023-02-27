package team.aliens.dms.domain.student.spi

import team.aliens.dms.domain.room.model.Room
import java.util.UUID

interface StudentQueryRoomPort {

    fun queryRoomBySchoolIdAndNumber(schoolId: UUID, number: String): Room?
}
