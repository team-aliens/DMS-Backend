package team.aliens.dms.domain.meal.service

import team.aliens.dms.common.annotation.Service

@Service
class MealService(
    queryMealService: QueryMealService,
    commandMealService: CommandMealService
) : QueryMealService by queryMealService,
    CommandMealService by commandMealService
