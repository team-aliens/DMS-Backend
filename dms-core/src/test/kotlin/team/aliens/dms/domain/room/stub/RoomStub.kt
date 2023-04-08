package team.aliens.dms.domain.room.stub

import team.aliens.dms.domain.room.model.Room
import java.util.UUID

internal fun createRoomStub(
    id: UUID = UUID.randomUUID(),
    number: String = "415",
    schoolId: UUID = UUID.randomUUID()
) = Room(
    id = id,
    number = number,
    schoolId = schoolId
)
