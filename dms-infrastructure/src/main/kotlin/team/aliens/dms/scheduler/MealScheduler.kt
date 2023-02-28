package team.aliens.dms.scheduler

import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import team.aliens.dms.domain.meal.usecase.SaveAllMealsUseCase

@Component
class MealScheduler(
    private val saveAllMealsUseCase: SaveAllMealsUseCase
) {

    @Scheduled(cron = "0 25 17 * * *", zone = "Asia/Seoul")
    fun saveAllMeals() = saveAllMealsUseCase.execute()
}
