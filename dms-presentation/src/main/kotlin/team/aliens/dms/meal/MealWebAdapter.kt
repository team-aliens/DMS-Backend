package team.aliens.dms.meal

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import team.aliens.dms.domain.meal.dto.MealDetailsResponse
import team.aliens.dms.domain.meal.usecase.MealDetailsUseCase
import java.time.LocalDate

@RequestMapping("/meals")
@RestController
class MealWebAdapter(
    private val mealDetailsUseCase: MealDetailsUseCase
) {

    @GetMapping("/{date}")
    fun details(@PathVariable(name = "date") mealDate: LocalDate): MealDetailsResponse {
        return mealDetailsUseCase.execute(mealDate)
    }
}