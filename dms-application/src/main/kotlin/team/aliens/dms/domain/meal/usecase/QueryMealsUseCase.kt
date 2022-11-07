package team.aliens.dms.domain.meal.usecase

import team.aliens.dms.common.annotation.ReadOnlyUseCase
import team.aliens.dms.domain.meal.dto.QueryMealsResponse
import team.aliens.dms.domain.meal.dto.QueryMealsResponse.MealDetails
import team.aliens.dms.domain.meal.spi.MealQueryStudentPort
import team.aliens.dms.domain.meal.spi.MealSecurityPort
import team.aliens.dms.domain.meal.spi.QueryMealPort
import team.aliens.dms.domain.student.exception.StudentNotFoundException
import java.time.LocalDate

@ReadOnlyUseCase
class QueryMealsUseCase(
    private val securityPort: MealSecurityPort,
    private val queryStudentPort: MealQueryStudentPort,
    private val queryMealPort: QueryMealPort
) {

    fun execute(mealDate: LocalDate): QueryMealsResponse {
        val currentUserId = securityPort.getCurrentUserId()
        val student = queryStudentPort.queryStudentById(currentUserId) ?: throw StudentNotFoundException
        val meals = queryMealPort.queryAllMealsByMealDateAndSchoolId(mealDate, student.schoolId)

        val mealDetails = meals.map {
            MealDetails(
                date = it.mealDate,
                breakfast = it.toSplit(it.breakfast),
                lunch = it.toSplit(it.lunch),
                dinner = it.toSplit(it.dinner),
                calInfo = it.calInfo
            )
        }

        return QueryMealsResponse(mealDetails)
    }
}
