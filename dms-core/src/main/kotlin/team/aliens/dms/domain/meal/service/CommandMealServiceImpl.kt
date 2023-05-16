package team.aliens.dms.domain.meal.service

import team.aliens.dms.common.annotation.Service
import team.aliens.dms.domain.meal.model.Meal
import team.aliens.dms.domain.meal.spi.CommandMealPort

@Service
class CommandMealServiceImpl(
    private val commandMealPort: CommandMealPort
) : CommandMealService {

    override fun saveAllMeals(meals: List<Meal>) {
        commandMealPort.saveAllMeals(meals)
    }
}
