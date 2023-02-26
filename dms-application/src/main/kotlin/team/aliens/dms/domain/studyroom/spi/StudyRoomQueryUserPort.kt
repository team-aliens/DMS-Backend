package team.aliens.dms.domain.studyroom.spi

import team.aliens.dms.domain.user.model.User
import java.util.UUID

interface StudyRoomQueryUserPort {

    fun queryUserById(userId: UUID): User?
}
