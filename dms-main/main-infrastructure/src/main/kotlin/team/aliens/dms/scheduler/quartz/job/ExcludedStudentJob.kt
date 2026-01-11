package team.aliens.dms.scheduler.quartz.job

import org.quartz.DisallowConcurrentExecution
import org.quartz.Job
import org.quartz.JobExecutionContext
import org.springframework.stereotype.Component
import team.aliens.dms.domain.vote.usecase.DeleteAllExcludedStudentsUseCase
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth
import java.time.temporal.TemporalAdjusters

@Component
@DisallowConcurrentExecution
class ExcludedStudentJob(
    private val deleteAllExcludedStudentsUseCase: DeleteAllExcludedStudentsUseCase
) : Job {
    override fun execute(context: JobExecutionContext) {
        val today = LocalDate.now()

        val lastMonday = YearMonth.from(today)
            .atEndOfMonth()
            .with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))

        if (today == lastMonday) {
            deleteAllExcludedStudentsUseCase.execute()
        }
    }
}
