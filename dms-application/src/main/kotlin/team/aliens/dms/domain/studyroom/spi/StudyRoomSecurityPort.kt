package team.aliens.dms.domain.studyroom.spi

import java.util.UUID

interface StudyRoomSecurityPort {

    fun getCurrentUserId(): UUID

}