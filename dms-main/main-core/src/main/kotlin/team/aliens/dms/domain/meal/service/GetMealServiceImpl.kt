package team.aliens.dms.domain.meal.service

import team.aliens.dms.common.annotation.Service
import team.aliens.dms.domain.meal.model.Meal
import team.aliens.dms.domain.meal.spi.QueryMealPort
import java.time.LocalDate
import java.util.UUID

@Service
class GetMealServiceImpl(
    private val queryMealPort: QueryMealPort
) : GetMealService {

    override fun getMealDetails(
        firstDay: LocalDate,
        lastDay: LocalDate,
        schoolId: UUID
    ): Map<LocalDate, Meal> {
        return queryMealPort.queryAllMealsByMonthAndSchoolId(
            firstDay, lastDay, schoolId
        ).associateBy { it.mealDate }
    }
}
