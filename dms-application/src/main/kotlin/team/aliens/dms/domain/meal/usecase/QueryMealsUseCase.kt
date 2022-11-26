package team.aliens.dms.domain.meal.usecase

import team.aliens.dms.common.annotation.ReadOnlyUseCase
import team.aliens.dms.common.extension.iterator
import team.aliens.dms.domain.meal.dto.QueryMealsResponse
import team.aliens.dms.domain.meal.dto.QueryMealsResponse.MealDetails
import team.aliens.dms.domain.meal.spi.MealQueryStudentPort
import team.aliens.dms.domain.meal.spi.MealSecurityPort
import team.aliens.dms.domain.meal.spi.QueryMealPort
import team.aliens.dms.domain.student.exception.StudentNotFoundException
import java.time.LocalDate
import java.time.YearMonth

@ReadOnlyUseCase
class QueryMealsUseCase(
    private val securityPort: MealSecurityPort,
    private val queryStudentPort: MealQueryStudentPort,
    private val queryMealPort: QueryMealPort
) {

    fun execute(mealDate: LocalDate): QueryMealsResponse {
        val currentUserId = securityPort.getCurrentUserId()
        val student = queryStudentPort.queryStudentById(currentUserId) ?: throw StudentNotFoundException

        val month = YearMonth.from(mealDate)
        val firstDay = month.atDay(1)
        val lastDay = month.atEndOfMonth()

        val mealMap = queryMealPort.queryAllMealsByMonthAndSchoolId(
            firstDay, lastDay, student.schoolId
        ).groupBy { it.mealDate }

        val mealDetails = mutableListOf<MealDetails>()
        for (date in firstDay..lastDay) {
            val meals = mealMap[date]

            if (meals == null) {
                mealDetails.add(MealDetails.emptyOf(date))
            } else {
                meals[0].apply {
                    mealDetails.add(MealDetails.of(this))
                }
            }
        }

        return QueryMealsResponse(mealDetails)
    }
}