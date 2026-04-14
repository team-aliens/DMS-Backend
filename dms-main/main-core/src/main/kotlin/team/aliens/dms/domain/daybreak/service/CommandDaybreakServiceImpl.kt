package team.aliens.dms.domain.daybreak.service

import team.aliens.dms.common.annotation.Service
import team.aliens.dms.domain.daybreak.model.DaybreakStudyApplication
import team.aliens.dms.domain.daybreak.model.DaybreakStudyType
import team.aliens.dms.domain.daybreak.model.Status
import team.aliens.dms.domain.daybreak.spi.CommandDaybreakStudyApplicationPort

@Service
class CommandDaybreakServiceImpl(
    private val commandDaybreakStudyApplicationPort: CommandDaybreakStudyApplicationPort,
) : CommandDaybreakService {

    override fun saveDaybreakStudyApplication(application: DaybreakStudyApplication) {
        commandDaybreakStudyApplicationPort.saveDaybreakStudyApplication(application)
    }

    override fun changeStatusDaybreakStudyApplication(status: Status) {
        commandDaybreakStudyApplicationPort.changeStatusDaybreakStudyApplication(status)
    }

    override fun saveDaybreakStudyType(type: DaybreakStudyType) {
        TODO()
    }
}
