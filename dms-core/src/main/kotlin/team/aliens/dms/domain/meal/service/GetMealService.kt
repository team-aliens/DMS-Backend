package team.aliens.dms.domain.meal.service

import team.aliens.dms.domain.meal.dto.QueryMealsResponse
import java.time.LocalDate
import java.util.UUID

interface GetMealService {
    fun queryMealDetails(firstDay: LocalDate, lastDay: LocalDate, schoolId: UUID): MutableList<QueryMealsResponse.MealDetails>
}
