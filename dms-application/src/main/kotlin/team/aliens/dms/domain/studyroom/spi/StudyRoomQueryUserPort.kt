package team.aliens.dms.domain.studyroom.spi

import java.util.UUID
import team.aliens.dms.domain.user.model.User

interface StudyRoomQueryUserPort {

    fun queryUserById(userId: UUID): User?

}