package team.aliens.dms.domain.meal.service

import team.aliens.dms.domain.meal.model.Meal

interface CommandMealService {
    fun saveAllMeals(meals: List<Meal>)
}
