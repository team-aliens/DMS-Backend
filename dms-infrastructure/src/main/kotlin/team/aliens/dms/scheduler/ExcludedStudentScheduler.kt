package team.aliens.dms.scheduler

import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import team.aliens.dms.domain.vote.usecase.ClearExcludedStudentsUseCase
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth
import java.time.temporal.TemporalAdjusters

@Component
class ExcludedStudentScheduler(
    private val clearExcludedStudentsUseCase: ClearExcludedStudentsUseCase,
) {

    @Scheduled(cron = "0 0 0 * * MON", zone = "Asia/Seoul")
    fun clearExcludedStudentsIfLastMonday() {
        val today = LocalDate.now()

        val lastMonday = YearMonth.from(today)
            .atEndOfMonth()
            .with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))

        if (today == lastMonday) {
            clearExcludedStudentsUseCase.execute()
        }
    }
}
