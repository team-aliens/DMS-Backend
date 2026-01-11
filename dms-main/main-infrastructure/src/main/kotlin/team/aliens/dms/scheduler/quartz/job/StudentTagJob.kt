package team.aliens.dms.scheduler.quartz.job

import org.quartz.DisallowConcurrentExecution
import org.quartz.Job
import org.quartz.JobExecutionContext
import org.springframework.stereotype.Component
import team.aliens.dms.domain.tag.usecase.UpdateStudentTagsUseCase

@Component
@DisallowConcurrentExecution
class StudentTagJob(
    private val updateStudentTagsUseCase: UpdateStudentTagsUseCase
) : Job {
    override fun execute(context: JobExecutionContext?) {
        updateStudentTagsUseCase.execute()
    }
}
