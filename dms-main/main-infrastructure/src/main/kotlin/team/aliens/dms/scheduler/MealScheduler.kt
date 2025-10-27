package team.aliens.dms.scheduler

import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import team.aliens.dms.domain.meal.usecase.SaveAllMealsUseCase

@Component
class MealScheduler(
    private val saveAllMealsUseCase: SaveAllMealsUseCase
) {

    /**
     * 매달 28일에 cron job 실행
     */
    @Scheduled(cron = "0 0 0 28 * *", zone = "Asia/Seoul")
    fun saveAllMeals() = saveAllMealsUseCase.execute()
}
