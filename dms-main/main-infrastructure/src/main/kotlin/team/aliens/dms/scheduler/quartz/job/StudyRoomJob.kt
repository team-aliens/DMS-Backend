package team.aliens.dms.scheduler.quartz.job

import org.quartz.DisallowConcurrentExecution
import org.quartz.Job
import org.quartz.JobExecutionContext
import org.springframework.stereotype.Component
import team.aliens.dms.domain.studyroom.usecase.ResetAllStudyRoomsUseCase

@Component
@DisallowConcurrentExecution
class StudyRoomJob(
    private val resetAllStudyRoomsUseCase: ResetAllStudyRoomsUseCase
) : Job {
    override fun execute(context: JobExecutionContext?) {
        resetAllStudyRoomsUseCase.execute()
    }
}
