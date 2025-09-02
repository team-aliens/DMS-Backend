package team.aliens.dms.domain.outing.service

import team.aliens.dms.common.annotation.Service
import team.aliens.dms.common.spi.NotificationEventPort
import team.aliens.dms.common.spi.SecurityPort
import team.aliens.dms.domain.outing.model.OutingApplication
import team.aliens.dms.domain.outing.model.OutingAvailableTime
import team.aliens.dms.domain.outing.model.OutingCompanion
import team.aliens.dms.domain.outing.model.OutingType
import team.aliens.dms.domain.outing.spi.CommandOutingApplicationPort
import team.aliens.dms.domain.outing.spi.CommandOutingCompanionPort
import team.aliens.dms.domain.outing.spi.CommandOutingTimePort
import team.aliens.dms.domain.outing.spi.CommandOutingTypePort
import java.util.UUID]

@Service
class CommandOutingServiceImpl(
    private val commandOutingApplicationPort: CommandOutingApplicationPort,
    private val commandOutingCompanionPort: CommandOutingCompanionPort,
    private val commandOutingTypePort: CommandOutingTypePort,
    private val commandOutingTimePort: CommandOutingTimePort,
    private val notificationEventPort: NotificationEventPort,
    private val securityPort: SecurityPort
) : CommandOutingService {

    override fun saveOutingApplication(outingApplication: OutingApplication): OutingApplication {
        val schoolId = securityPort.getCurrentUserSchoolId()
        val savedOutingApplication = commandOutingApplicationPort.saveOutingApplication(outingApplication)
            .copy(companionIds = outingApplication.companionIds)

        val deviceTokens: List<DeviceToken>? = outingApplication.companionIds?.let {
            queryDeviceTokenPort.queryDeviceTokensByStudentIds(it)
        }

        class OutingNotification(
            schoolId: UUID,
            outing: OutingApplication
        ) : Notification(
            schoolId = schoolId,
            topic = Topic.OUTING,
            linkIdentifier = outing.id.toString(),
            title = "외출이 신청되었습니다",
            content = "외출 시간은 " + outing.outingTime + " ~ " + outing.arrivalTime + "입니다",
            threadId = outing.id.toString(),
            isSaveRequired = true
        )

        return savedOutingApplication
            .also {
                deviceTokens?.let {
                    notificationEventPort.publishNotificationToApplicant(
                        it, Notification.OutingNotification(schoolId, outingApplication)
                    )
                }
                saveAllOutingCompanions(savedOutingApplication)
            }
    }

    private fun saveAllOutingCompanions(outingApplication: OutingApplication) {
        val companionIds = outingApplication.companionIds
        if (!companionIds.isNullOrEmpty()) {
            val outingCompanions = companionIds.map {
                OutingCompanion(
                    outingApplicationId = outingApplication.id,
                    studentId = it
                )
            }

            commandOutingCompanionPort.saveAllOutingCompanions(outingCompanions)
        }
    }

    override fun saveOutingType(outingType: OutingType): OutingType =
        commandOutingTypePort.saveOutingType(outingType)

    override fun deleteOutingType(outingType: OutingType) {
        commandOutingTypePort.deleteOutingType(outingType)
    }

    override fun deleteOutingApplication(outingApplication: OutingApplication) {
        commandOutingApplicationPort.deleteOutingApplication(outingApplication)
    }

    override fun saveOutingAvailableTime(outingAvailableTime: OutingAvailableTime): OutingAvailableTime =
        commandOutingTimePort.saveOutingAvailableTime(outingAvailableTime)

    override fun deleteOutingAvailableTime(outingAvailableTime: OutingAvailableTime) {
        commandOutingTimePort.deleteOutingAvailableTime(outingAvailableTime)
    }
}
