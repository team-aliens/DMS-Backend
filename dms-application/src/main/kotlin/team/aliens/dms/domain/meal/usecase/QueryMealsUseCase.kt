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
            MealDetail(
                date = it.mealDate,
                breakfast = it.breakfast?.split("||").orEmpty(),
                lunch = it.lunch?.split("||").orEmpty(),
                dinner = it.dinner?.split("||").orEmpty()
            )
        }

        return QueryMealsResponse(mealDetails)
    }
}