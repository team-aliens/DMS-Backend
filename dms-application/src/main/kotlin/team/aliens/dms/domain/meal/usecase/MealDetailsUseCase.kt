package team.aliens.dms.domain.meal.usecase

import team.aliens.dms.domain.meal.dto.MealDetailsResponse
import team.aliens.dms.domain.meal.dto.MealDetailsResponse.Meal
import team.aliens.dms.domain.meal.exception.MealNotFoundException
import team.aliens.dms.domain.meal.spi.MealQueryStudentSecurityPort
import team.aliens.dms.domain.meal.spi.MealQueryUserPort
import team.aliens.dms.domain.meal.spi.QueryMealPort
import team.aliens.dms.domain.student.exception.StudentNotFoundException
import team.aliens.dms.global.annotation.ReadOnlyUseCase
import java.time.LocalDate

@ReadOnlyUseCase
class MealDetailsUseCase(
    private val queryStudentSecurityPort: MealQueryStudentSecurityPort,
    private val queryUserPort: MealQueryUserPort,
    private val queryMealPort: QueryMealPort
) {

    fun execute(mealDate: LocalDate): MealDetailsResponse {
        val studentId = queryStudentSecurityPort.getCurrentUserId()
        val student = queryUserPort.queryByUserId(studentId) ?: throw StudentNotFoundException
        val meal = queryMealPort.queryAllById(mealDate, student.schoolId)

        val meals: List<Meal> = meal.map {
            Meal(
                date = it.mealDate,
                breakfast = listOf(it.breakfast ?: throw MealNotFoundException),
                lunch = listOf(it.lunch ?: throw MealNotFoundException),
                dinner = listOf(it.dinner ?: throw MealNotFoundException)
            )
        }

        return MealDetailsResponse(meals)
    }
}