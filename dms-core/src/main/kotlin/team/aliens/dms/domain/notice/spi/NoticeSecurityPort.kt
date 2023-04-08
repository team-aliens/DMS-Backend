package team.aliens.dms.domain.notice.spi

import java.util.UUID

interface NoticeSecurityPort {

    fun getCurrentUserId(): UUID
}
