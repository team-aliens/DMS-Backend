package team.aliens.dms.domain.student.spi

import java.util.UUID
import team.aliens.dms.domain.room.model.Room

interface StudentQueryRoomPort {

    fun queryRoomBySchoolIdAndNumber(schoolId: UUID, number: Int): Room?

}