package team.aliens.dms.domain.notification.service

import team.aliens.dms.common.annotation.Service
import team.aliens.dms.domain.notification.spi.QueryDeviceTokenPort
import java.util.UUID

@Service
class CheckNotificationServiceImpl(
    private val deviceTokenPort: QueryDeviceTokenPort
) : CheckNotificationService {

    override fun checkDeviceTokenByUserId(userId: UUID): Boolean {
        return deviceTokenPort.existsDeviceTokenByUserId(userId)
    }
}
