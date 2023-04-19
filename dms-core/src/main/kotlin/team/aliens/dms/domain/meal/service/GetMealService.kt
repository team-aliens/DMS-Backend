package team.aliens.dms.domain.meal.service

import team.aliens.dms.domain.meal.model.Meal
import java.time.LocalDate
import java.util.UUID

interface GetMealService {
    fun getMealDetails(firstDay: LocalDate, lastDay: LocalDate, schoolId: UUID): Map<LocalDate, Meal>
}
