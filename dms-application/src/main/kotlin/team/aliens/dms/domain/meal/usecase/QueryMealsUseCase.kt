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
                breakfast = toSplit(it.breakfast),
                lunch = toSplit(it.lunch),
                dinner = toSplit(it.dinner)
            )
        }

        return QueryMealsResponse(mealDetails)
    }

    /**
     * '||' 를 기준으로 급식 구분
     **/
    private fun toSplit(meal: String?) = meal?.split("||").orEmpty()
}
