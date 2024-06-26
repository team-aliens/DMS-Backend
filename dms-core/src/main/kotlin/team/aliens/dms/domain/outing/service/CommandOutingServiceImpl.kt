package team.aliens.dms.domain.outing.service

import team.aliens.dms.common.annotation.Service
import team.aliens.dms.domain.outing.model.OutingApplication
import team.aliens.dms.domain.outing.model.OutingAvailableTime
import team.aliens.dms.domain.outing.model.OutingCompanion
import team.aliens.dms.domain.outing.model.OutingType
import team.aliens.dms.domain.outing.spi.CommandOutingApplicationPort
import team.aliens.dms.domain.outing.spi.CommandOutingCompanionPort
import team.aliens.dms.domain.outing.spi.CommandOutingTimePort
import team.aliens.dms.domain.outing.spi.CommandOutingTypePort

@Service
class CommandOutingServiceImpl(
    private val commandOutingApplicationPort: CommandOutingApplicationPort,
    private val commandOutingCompanionPort: CommandOutingCompanionPort,
    private val commandOutingTypePort: CommandOutingTypePort,
    private val commandOutingTimePort: CommandOutingTimePort
) : CommandOutingService {

    override fun saveOutingApplication(outingApplication: OutingApplication): OutingApplication {
        val savedOutingApplication = commandOutingApplicationPort.saveOutingApplication(outingApplication)
            .copy(companionIds = outingApplication.companionIds)

        saveAllOutingCompanions(savedOutingApplication)

        return savedOutingApplication
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
