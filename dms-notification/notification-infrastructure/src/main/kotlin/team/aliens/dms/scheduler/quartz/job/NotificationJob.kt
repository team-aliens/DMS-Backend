package team.aliens.dms.scheduler.quartz.job

import org.quartz.DisallowConcurrentExecution
import org.quartz.Job
import org.quartz.JobExecutionContext
import org.springframework.stereotype.Component
import team.aliens.dms.domain.notification.usecase.DeleteOldNotificationsUseCase

@Component
@DisallowConcurrentExecution
class NotificationJob(
    private val deleteOldNotificationsUseCase: DeleteOldNotificationsUseCase
) : Job {
    override fun execute(context: JobExecutionContext?) {
        deleteOldNotificationsUseCase.execute()
    }
}
