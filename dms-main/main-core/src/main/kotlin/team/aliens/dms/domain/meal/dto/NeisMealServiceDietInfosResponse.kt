package team.aliens.dms.domain.meal.dto

import java.time.LocalDate

data class NeisMealServiceDietInfosResponse(
    val mealServiceDietInfos: List<MealServiceDietInfoElement>
) {
    data class MealServiceDietInfoElement(
        val mealDate: LocalDate,
        val breakfast: String,
        val lunch: String,
        val dinner: String
    )
}
