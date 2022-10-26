package team.aliens.dms.domain.meal.exception

import team.aliens.dms.domain.meal.error.MealErrorCode
import team.aliens.dms.global.error.DmsException

object MealNotFoundException : DmsException(
    MealErrorCode.MEAL_NOT_FOUND
)