package team.aliens.dms.scheduler.quartz.job

import org.quartz.DisallowConcurrentExecution
import org.quartz.Job
import org.quartz.JobExecutionContext
import org.springframework.stereotype.Component
import team.aliens.dms.domain.daybreak.usecase.CloseDaybreakStudyApplicationUseCase

@Component
@DisallowConcurrentExecution
class DaybreakStudyApplicationJob(
    private val useCase: CloseDaybreakStudyApplicationUseCase
) : Job {
    override fun execute(context: JobExecutionContext?) {
        useCase.execute()
    }
}
