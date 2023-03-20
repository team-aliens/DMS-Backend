package team.aliens.dms.domain.meal.stub

import team.aliens.dms.domain.meal.model.Meal
import java.time.LocalDate
import java.util.UUID

internal fun createMealStub(
    mealDate: LocalDate = LocalDate.now(),
    schoolId: UUID = UUID.randomUUID(),
    breakfast: String = "아침",
    lunch: String = "점심",
    dinner: String = "저녁"
) = Meal(
    mealDate = mealDate,
    schoolId = schoolId,
    breakfast = breakfast,
    lunch = lunch,
    dinner = dinner
)
