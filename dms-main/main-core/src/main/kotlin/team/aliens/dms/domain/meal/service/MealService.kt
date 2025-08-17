package team.aliens.dms.domain.meal.service

import team.aliens.dms.common.annotation.Service

@Service
class MealService(
    getMealService: GetMealService,
    commandMealService: CommandMealService
) : GetMealService by getMealService,
    CommandMealService by commandMealService
