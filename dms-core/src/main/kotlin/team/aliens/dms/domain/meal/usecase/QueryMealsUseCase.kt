package team.aliens.dms.domain.meal.usecase

import team.aliens.dms.common.annotation.ReadOnlyUseCase
import team.aliens.dms.domain.meal.dto.QueryMealsResponse
import team.aliens.dms.domain.meal.service.MealService
import team.aliens.dms.domain.user.service.UserService
import java.time.LocalDate
import java.time.YearMonth

@ReadOnlyUseCase
class QueryMealsUseCase(
    private val userService: UserService,
    private val mealService: MealService
) {

    fun execute(mealDate: LocalDate): QueryMealsResponse {
        val user = userService.getCurrentUser()

        val month = YearMonth.from(mealDate)
        val firstDay = month.atDay(1)
        val lastDay = month.atEndOfMonth()

        val mealDetails = mealService.queryMealDetails(
            firstDay, lastDay, user.schoolId
        )

        return QueryMealsResponse(mealDetails)
    }
}
