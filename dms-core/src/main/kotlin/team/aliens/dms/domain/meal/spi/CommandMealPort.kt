package team.aliens.dms.domain.meal.spi

import team.aliens.dms.domain.meal.model.Meal

interface CommandMealPort {

    fun saveAllMeals(meals: List<Meal>)
}
