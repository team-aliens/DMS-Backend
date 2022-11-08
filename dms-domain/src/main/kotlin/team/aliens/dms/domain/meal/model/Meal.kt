package team.aliens.dms.domain.meal.model

import team.aliens.dms.common.annotation.Aggregate
import java.time.LocalDate
import java.util.UUID

@Aggregate
data class Meal(

    val mealDate: LocalDate,

    val schoolId: UUID,

    val breakfast: String?,

    val lunch: String?,

    val dinner: String?

) {

    /**
     * '||' 를 기준으로 급식 구분
     **/
    fun toSplit(meal: String?) = meal?.split("||").orEmpty()
}