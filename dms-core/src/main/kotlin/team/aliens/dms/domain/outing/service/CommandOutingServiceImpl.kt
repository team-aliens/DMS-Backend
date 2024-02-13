package team.aliens.dms.domain.outing.service

import team.aliens.dms.common.annotation.Service
import team.aliens.dms.domain.outing.model.OutingApplication
import team.aliens.dms.domain.outing.model.OutingCompanion
import team.aliens.dms.domain.outing.model.OutingType
import team.aliens.dms.domain.outing.spi.CommandOutingApplicationPort
import team.aliens.dms.domain.outing.spi.CommandOutingCompanionPort
import team.aliens.dms.domain.outing.spi.CommandOutingTypePort

@Service
class CommandOutingServiceImpl(
    private val commandOutingApplicationPort: CommandOutingApplicationPort,
    private val commandOutingCompanionPort: CommandOutingCompanionPort,
    private val commandOutingTypePort: CommandOutingTypePort,
) : CommandOutingService {

    override fun apply(outingApplication: OutingApplication): OutingApplication {
        val savedOutingApplication = commandOutingApplicationPort.saveOutingApplication(outingApplication)

        val companionIds = outingApplication.companionIds
        if (!companionIds.isNullOrEmpty()) {
            val outingCompanions = companionIds.map {
                OutingCompanion(
                    outingApplicationId = savedOutingApplication.id,
                    studentId = it
                )
            }

            commandOutingCompanionPort.saveAllOutingCompanions(outingCompanions)
        }

        return savedOutingApplication
    }
    override fun saveOutingType(outingType: OutingType): OutingType =
        commandOutingTypePort.saveOutingType(outingType)
}
