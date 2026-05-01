package team.aliens.dms.scheduler.quartz.job

import org.quartz.DisallowConcurrentExecution
import org.quartz.Job
import org.quartz.JobExecutionContext
import org.springframework.stereotype.Component
import team.aliens.dms.domain.daybreak.usecase.ExpireDaybreakStudyApplicationUseCase

@Component
@DisallowConcurrentExecution
class DaybreakStudyApplicationJob(
    private val useCase: ExpireDaybreakStudyApplicationUseCase
) : Job{
    override fun execute(p0: JobExecutionContext?) {
        useCase.execute()
    }
}