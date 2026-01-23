package team.aliens.dms.scheduler.quartz.job

import org.quartz.DisallowConcurrentExecution
import org.quartz.Job
import org.quartz.JobExecutionContext
import org.springframework.stereotype.Component
import team.aliens.dms.domain.volunteer.usecase.ConvertVolunteerScoreToPointUseCase

@Component
@DisallowConcurrentExecution
class VolunteerScoreJob(
    private val convertVolunteerScoreToPointUseCase: ConvertVolunteerScoreToPointUseCase
) : Job {
    override fun execute(context: JobExecutionContext?) {
        convertVolunteerScoreToPointUseCase.execute()
    }
}
