package team.aliens.dms.domain.meal.model

import team.aliens.dms.global.annotation.Aggregate
import java.time.LocalDate
import java.util.UUID

@Aggregate
data class Meal(

    val mealDate: LocalDate,

    val schoolId: UUID,

    val breakfast: String?,

    val lunch: String?,

    val dinner: String?
)