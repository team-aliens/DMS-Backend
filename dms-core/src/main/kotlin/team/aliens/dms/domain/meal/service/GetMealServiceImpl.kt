package team.aliens.dms.domain.meal.service

import team.aliens.dms.common.annotation.Service
import team.aliens.dms.common.extension.iterator
import team.aliens.dms.domain.meal.dto.QueryMealsResponse
import team.aliens.dms.domain.meal.spi.QueryMealPort
import java.time.LocalDate
import java.util.UUID

@Service
class GetMealServiceImpl(
    private val queryMealPort: QueryMealPort
) : GetMealService {

    override fun queryMealDetails(
        firstDay: LocalDate,
        lastDay: LocalDate,
        schoolId: UUID
    ): MutableList<QueryMealsResponse.MealDetails> {
        val mealMap = queryMealPort.queryAllMealsByMonthAndSchoolId(
            firstDay, lastDay, schoolId
        ).associateBy { it.mealDate }

        val mealDetails = mutableListOf<QueryMealsResponse.MealDetails>()
        for (date in firstDay..lastDay) {
            val meal = mealMap[date]

            if (meal == null) {
                mealDetails.add(QueryMealsResponse.MealDetails.emptyOf(date))
            } else {
                mealDetails.add(QueryMealsResponse.MealDetails.of(meal))
            }
        }

        return mealDetails
    }
}
