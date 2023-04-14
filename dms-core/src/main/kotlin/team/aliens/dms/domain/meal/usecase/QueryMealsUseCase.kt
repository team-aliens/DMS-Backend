package team.aliens.dms.domain.meal.usecase

import java.time.LocalDate
import java.time.YearMonth
import team.aliens.dms.common.annotation.ReadOnlyUseCase
import team.aliens.dms.common.extension.iterator
import team.aliens.dms.domain.meal.dto.QueryMealsResponse
import team.aliens.dms.domain.meal.dto.QueryMealsResponse.MealDetails
import team.aliens.dms.domain.meal.spi.QueryMealPort
import team.aliens.dms.domain.user.service.GetUserService

@ReadOnlyUseCase
class QueryMealsUseCase(
    private val getUserService: GetUserService,
    private val queryMealPort: QueryMealPort
) {

    fun execute(mealDate: LocalDate): QueryMealsResponse {
        val student = getUserService.getCurrentStudent()

        val month = YearMonth.from(mealDate)
        val firstDay = month.atDay(1)
        val lastDay = month.atEndOfMonth()

        val mealMap = queryMealPort.queryAllMealsByMonthAndSchoolId(
            firstDay, lastDay, student.schoolId
        ).associateBy { it.mealDate }

        val mealDetails = mutableListOf<MealDetails>()
        for (date in firstDay..lastDay) {
            val meal = mealMap[date]

            if (meal == null) {
                mealDetails.add(MealDetails.emptyOf(date))
            } else {
                mealDetails.add(MealDetails.of(meal))
            }
        }

        return QueryMealsResponse(mealDetails)
    }
}
