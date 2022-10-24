package team.aliens.dms.domain.meal.usecase

import team.aliens.dms.domain.meal.dto.QueryMealsResponse
import team.aliens.dms.domain.meal.dto.QueryMealsResponse.MealDetail
import team.aliens.dms.domain.meal.spi.MealQueryStudentPort
import team.aliens.dms.domain.meal.spi.MealSecurityPort
import team.aliens.dms.domain.meal.spi.QueryMealPort
import team.aliens.dms.domain.student.exception.StudentNotFoundException
import team.aliens.dms.global.annotation.ReadOnlyUseCase
import java.time.LocalDate

@ReadOnlyUseCase
class QueryMealsUseCase(
    private val securityPort: MealSecurityPort,
    private val queryStudentPort: MealQueryStudentPort,
    private val queryMealPort: QueryMealPort
) {

    fun execute(mealDate: LocalDate): QueryMealsResponse {
        val userId = securityPort.getCurrentUserId()
        val student = queryStudentPort.queryByUserId(userId) ?: throw StudentNotFoundException
        val meals = queryMealPort.queryAllByMealDateAndSchoolId(mealDate, student.schoolId)

        val mealDetails = meals.map {
            val breakfast = it.breakfast?.split("||")
            val lunch = it.lunch?.split("||")
            val dinner = it.dinner?.split("||")

            MealDetail(
                date = it.mealDate,
                breakfast = breakfast.orEmpty(),
                lunch = lunch.orEmpty(),
                dinner = dinner.orEmpty()
            )
        }

        return QueryMealsResponse(mealDetails)
    }
}