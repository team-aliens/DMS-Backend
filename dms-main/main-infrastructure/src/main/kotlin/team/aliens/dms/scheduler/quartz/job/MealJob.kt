package team.aliens.dms.scheduler.quartz.job

import org.quartz.DisallowConcurrentExecution
import org.quartz.Job
import org.quartz.JobExecutionContext
import org.springframework.stereotype.Component
import team.aliens.dms.domain.meal.usecase.SaveAllMealsUseCase

@Component
@DisallowConcurrentExecution
class MealJob(
    private val saveAllMealsUseCase: SaveAllMealsUseCase
) : Job {
    override fun execute(context: JobExecutionContext?) {
        saveAllMealsUseCase.execute()
    }
}
