package team.aliens.dms.domain.notification.spi

import team.aliens.dms.domain.notification.model.NotificationOutbox
import java.util.UUID

interface QueryNotificationOutboxPort {

    // 다른 스레드·인스턴스가 처리 중인 행(잠금)은 건너뛰고 null 을 돌려준다
    fun queryPendingNotificationOutboxByIdForUpdate(id: UUID): NotificationOutbox?

    fun queryPendingNotificationOutboxIds(limit: Int): List<UUID>
}
