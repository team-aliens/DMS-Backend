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
    companion object {
        const val emptyMeal = "급식이 없습니다."
    }

    /**
     * '||' 를 기준으로 급식 구분
     **/
    fun toSplit(meal: String?) = if (!meal.isNullOrEmpty()) {
        meal.split("||")
    } else {
        listOf(emptyMeal)
    }
}
