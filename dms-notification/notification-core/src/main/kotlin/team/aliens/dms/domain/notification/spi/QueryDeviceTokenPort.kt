package team.aliens.dms.domain.notification.spi

import team.aliens.dms.contract.model.Topic
import team.aliens.dms.domain.notification.model.DeviceToken
import java.util.UUID

interface QueryDeviceTokenPort {

    fun existsDeviceTokenByUserId(userId: UUID): Boolean

    fun queryDeviceTokenByUserId(userId: UUID): DeviceToken?

    fun queryDeviceTokensByUserIds(userIds: List<UUID>): List<DeviceToken>

    fun queryDeviceTokenByToken(token: String): DeviceToken?

    fun queryDeviceTokensBySubscriptionTopicAndSchoolId(topic: Topic, schoolId: UUID): List<DeviceToken>
}
