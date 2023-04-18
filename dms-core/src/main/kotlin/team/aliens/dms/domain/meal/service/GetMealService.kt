package team.aliens.dms.domain.meal.service

import java.time.LocalDate
import java.util.UUID
import team.aliens.dms.domain.meal.model.Meal

interface GetMealService {
    fun getMealDetails(firstDay: LocalDate, lastDay: LocalDate, schoolId: UUID): Map<LocalDate, Meal>
}
