package team.aliens.dms.scheduler

import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import team.aliens.dms.domain.vote.usecase.DeleteAllExcludedStudentsUseCase
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth
import java.time.temporal.TemporalAdjusters

@Component
class ExcludedStudentScheduler(
    private val deleteAllExcludedStudentsUseCase: DeleteAllExcludedStudentsUseCase,
) {

    @Scheduled(cron = "0 0 0 * * MON", zone = "Asia/Seoul")
    fun deleteAllExcludedStudentsIfLastMonday() {
        val today = LocalDate.now()

        val lastMonday = YearMonth.from(today)
            .atEndOfMonth()
            .with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))

        if (today == lastMonday) {
            deleteAllExcludedStudentsUseCase.execute()
        }
    }
}
