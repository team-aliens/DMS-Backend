package team.aliens.dms.event

import org.springframework.context.ApplicationEvent
import team.aliens.dms.contract.model.notification.DeviceTokenInfo
import java.util.UUID

// 알림 outbox 행이 비즈니스 트랜잭션과 함께 저장됐다는 신호. 발송 내용은 DB 의 행에 있다
class NotificationOutboxSavedEvent(
    val outboxId: UUID
) : ApplicationEvent(outboxId)

sealed class DeviceTokenEvent(
    source: Any
) : ApplicationEvent(source)

class SaveDeviceTokenEvent(
    val deviceTokenInfo: DeviceTokenInfo
) : DeviceTokenEvent(deviceTokenInfo)

class DeleteDeviceTokenEvent(
    val userId: UUID
) : DeviceTokenEvent(userId)
