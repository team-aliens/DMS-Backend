package team.aliens.dms.domain.outing.service

import team.aliens.dms.common.annotation.Service
import team.aliens.dms.common.spi.NotificationEventPort
import team.aliens.dms.common.spi.SecurityPort
import team.aliens.dms.contract.model.notification.NotificationInfo
import team.aliens.dms.contract.model.notification.Topic
import team.aliens.dms.domain.outing.model.OutingApplication
import team.aliens.dms.domain.outing.model.OutingAvailableTime
import team.aliens.dms.domain.outing.model.OutingCompanion
import team.aliens.dms.domain.outing.model.OutingType
import team.aliens.dms.domain.outing.spi.CommandOutingApplicationPort
import team.aliens.dms.domain.outing.spi.CommandOutingCompanionPort
import team.aliens.dms.domain.outing.spi.CommandOutingTimePort
import team.aliens.dms.domain.outing.spi.CommandOutingTypePort
import team.aliens.dms.domain.user.spi.QueryUserPort

@Service
class CommandOutingServiceImpl(
    private val commandOutingApplicationPort: CommandOutingApplicationPort,
    private val commandOutingCompanionPort: CommandOutingCompanionPort,
    private val commandOutingTypePort: CommandOutingTypePort,
    private val commandOutingTimePort: CommandOutingTimePort,
    private val notificationEventPort: NotificationEventPort,
    private val securityPort: SecurityPort,
    private val queryUserPort: QueryUserPort
) : CommandOutingService {

    override fun saveOutingApplication(outingApplication: OutingApplication): OutingApplication {
        val schoolId = securityPort.getCurrentUserSchoolId()
        val savedOutingApplication = commandOutingApplicationPort.saveOutingApplication(outingApplication)
            .copy(companionIds = outingApplication.companionIds)

        val userIds = queryUserPort.queryUsersBySchoolId(schoolId)
            .map { it.id }

        return savedOutingApplication
            .also {
                notificationEventPort.publishNotificationToApplicant(
                    userIds,
                    NotificationInfo(
                        schoolId = schoolId,
                        topic = Topic.OUTING,
                        linkIdentifier = savedOutingApplication.id.toString(),
                        title = "외출이 신청되었습니다",
                        content = """외출 시간은 ${savedOutingApplication.outingTime} ~ ${savedOutingApplication.arrivalTime}입니다""",
                        threadId = savedOutingApplication.id.toString(),
                        isSaveRequired = true
                    )
                )

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
