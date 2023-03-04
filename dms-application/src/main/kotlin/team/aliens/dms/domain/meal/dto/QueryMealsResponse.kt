package team.aliens.dms.domain.meal.dto

import team.aliens.dms.domain.meal.model.Meal
import team.aliens.dms.domain.meal.model.Meal.Companion.emptyMeal
import java.time.LocalDate

data class QueryMealsResponse(
    val meals: List<MealDetails>
) {
    data class MealDetails(
        val date: LocalDate,
        val breakfast: List<String>,
        val lunch: List<String>,
        val dinner: List<String>
    ) {
        companion object {
            fun emptyOf(date: LocalDate) = MealDetails(
                date = date,
                breakfast = listOf(emptyMeal),
                lunch = listOf(emptyMeal),
                dinner = listOf(emptyMeal)
            )

            fun of(meal: Meal) = meal.run {
                MealDetails(
                    date = mealDate,
                    breakfast = toSplit(breakfast),
                    lunch = toSplit(lunch),
                    dinner = toSplit(dinner)
                )
            }
        }
    }
}
