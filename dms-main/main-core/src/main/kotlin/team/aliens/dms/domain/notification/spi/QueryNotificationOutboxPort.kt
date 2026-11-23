package team.aliens.dms.domain.notification.spi

import team.aliens.dms.domain.notification.model.NotificationOutbox
import java.util.UUID

interface QueryNotificationOutboxPort {

    // 행을 잠근다. 다른 쪽이 잡고 있으면 풀릴 때까지 기다리고, 그 사이 지워졌거나 PENDING 이 아니면 null 을 돌려준다
    fun queryPendingNotificationOutboxByIdForUpdate(id: UUID): NotificationOutbox?

    fun queryPendingNotificationOutboxIds(limit: Int): List<UUID>
}
