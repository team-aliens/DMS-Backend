package team.aliens.dms.domain.meal.error

import team.aliens.dms.global.error.ErrorProperty

enum class MealErrorCode(
    private val status: Int,
    private val message: String
) : ErrorProperty {

    MEAL_NOT_FOUND(404, "Meal Not Found")
    ;

    override fun status(): Int = status
    override fun message(): String = message
}