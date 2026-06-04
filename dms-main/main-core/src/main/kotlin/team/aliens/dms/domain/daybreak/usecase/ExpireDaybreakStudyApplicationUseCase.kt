package team.aliens.dms.domain.daybreak.usecase

import team.aliens.dms.common.annotation.SchedulerUseCase
import team.aliens.dms.domain.daybreak.model.Status
import team.aliens.dms.domain.daybreak.service.DaybreakService

@SchedulerUseCase
class ExpireDaybreakStudyApplicationUseCase(
    private val daybreakService: DaybreakService
) {

    fun execute() {
        val applications = daybreakService.findExpiredDaybreakStudyApplications()

        applications.forEach {
            it.status = Status.EXPIRED
        }

        daybreakService.saveAllDaybreakStudyApplications(applications)
    }
}
