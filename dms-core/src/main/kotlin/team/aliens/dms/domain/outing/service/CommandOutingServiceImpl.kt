package team.aliens.dms.domain.outing.service

import team.aliens.dms.common.annotation.Service
import team.aliens.dms.domain.outing.model.OutingApplication
import team.aliens.dms.domain.outing.spi.CommandOutingApplicationPort

@Service
class CommandOutingServiceImpl(
    private val commandOutingApplicationPort: CommandOutingApplicationPort
) : CommandOutingService {

    override fun saveOutingApplication(outingApplication: OutingApplication): OutingApplication =
        commandOutingApplicationPort.saveOutingApplication(outingApplication)

}
