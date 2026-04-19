package team.aliens.dms.domain.daybreak.service

import team.aliens.dms.common.annotation.Service
import team.aliens.dms.domain.daybreak.model.DaybreakStudyApplication
import team.aliens.dms.domain.daybreak.model.DaybreakStudyType
import team.aliens.dms.domain.daybreak.spi.CommandDaybreakStudyApplicationPort
import team.aliens.dms.domain.daybreak.spi.CommandDaybreakStudyTypePort

@Service
class CommandDaybreakServiceImpl(
    private val commandDaybreakStudyApplicationPort: CommandDaybreakStudyApplicationPort,
    private val commandDaybreakStudyTypePort: CommandDaybreakStudyTypePort
) : CommandDaybreakService {

    override fun saveDaybreakStudyApplication(application: DaybreakStudyApplication) {
        commandDaybreakStudyApplicationPort.saveDaybreakStudyApplication(application)
    }

    override fun saveDaybreakStudyType(type: DaybreakStudyType) {
        commandDaybreakStudyTypePort.saveDaybreakStudyType(type)
    }

    override fun saveAllDaybreakStudyApplications(applications: List<DaybreakStudyApplication>) {
        commandDaybreakStudyApplicationPort.saveAllDaybreakStudyApplications(applications)
    }
}
