package team.aliens.dms.domain.meal.usecase

import team.aliens.dms.domain.meal.dto.QueryMealsResponse
import team.aliens.dms.domain.meal.dto.QueryMealsResponse.MealDetail
import team.aliens.dms.domain.meal.spi.MealSecurityPort
import team.aliens.dms.domain.meal.spi.MealQueryUserPort
import team.aliens.dms.domain.meal.spi.QueryMealPort
import team.aliens.dms.domain.student.exception.StudentNotFoundException
import team.aliens.dms.global.annotation.ReadOnlyUseCase
import java.time.LocalDate

@ReadOnlyUseCase
class QueryMealsUseCase(
    private val securityPort: MealSecurityPort,
    private val queryUserPort: MealQueryUserPort,
    private val queryMealPort: QueryMealPort
) {

    fun execute(mealDate: LocalDate): QueryMealsResponse {
        val studentId = securityPort.getCurrentUserId()
        val user = queryUserPort.queryByUserId(studentId) ?: throw StudentNotFoundException
        val meal = queryMealPort.queryAllByMealDateAndSchoolId(mealDate, user.schoolId)

        val meals = meal.map {
            val breakfast = it.breakfast?.replace("||", "\",\"")
            val lunch = it.lunch?.replace("||", "\",\"")
            val dinner = it.dinner?.replace("||", "\",\"")

            MealDetail(
                date = it.mealDate,
                breakfast = listOf(breakfast!!),
                lunch = listOf(lunch!!),
                dinner = listOf(dinner!!)
            )
        }

        return QueryMealsResponse(meals)
    }
}