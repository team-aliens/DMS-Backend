package team.aliens.dms.domain.meal.dto

import java.time.LocalDate

data class QueryMealsResponse(
    val meals: List<MealDetails>
) {
    data class MealDetails(
        val date: LocalDate,
        val breakfast: List<String?>,
        val lunch: List<String?>,
        val dinner: List<String?>,
        val calInfo: String?
    )
}