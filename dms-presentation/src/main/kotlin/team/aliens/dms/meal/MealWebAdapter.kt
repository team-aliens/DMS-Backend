package team.aliens.dms.meal

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import team.aliens.dms.domain.meal.dto.QueryMealsResponse
import team.aliens.dms.domain.meal.usecase.QueryMealsUseCase
import java.time.LocalDate

@RequestMapping("/meals")
@RestController
class MealWebAdapter(
    private val queryMealsUseCase: QueryMealsUseCase
) {

    @GetMapping("/{date}")
    fun getMeals(@PathVariable("date") mealDate: LocalDate): QueryMealsResponse {
        return queryMealsUseCase.execute(mealDate)
    }
}