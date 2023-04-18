package team.aliens.dms.domain.meal.usecase

import team.aliens.dms.common.annotation.ReadOnlyUseCase
import team.aliens.dms.domain.meal.dto.QueryMealsResponse
import team.aliens.dms.domain.meal.service.MealService
import team.aliens.dms.domain.user.service.UserService
import java.time.LocalDate
import java.time.YearMonth
import team.aliens.dms.common.extension.iterator

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

        val mealMap = mealService.getMealDetails(
            firstDay, lastDay, user.schoolId
        )

        val mealDetails = mutableListOf<QueryMealsResponse.MealDetails>()
        for (date in firstDay..lastDay) {
            val meal = mealMap[date]

            if (meal == null) {
                mealDetails.add(QueryMealsResponse.MealDetails.emptyOf(date))
            } else {
                mealDetails.add(QueryMealsResponse.MealDetails.of(meal))
            }
        }

        return QueryMealsResponse(mealDetails)
    }
}
