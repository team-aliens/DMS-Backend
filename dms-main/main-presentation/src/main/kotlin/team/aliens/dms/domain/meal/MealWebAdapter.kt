package team.aliens.dms.domain.meal

import jakarta.validation.constraints.NotNull
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import team.aliens.dms.domain.meal.dto.QueryMealsResponse
import team.aliens.dms.domain.meal.usecase.QueryMealsUseCase
import java.time.LocalDate

@Validated
@RequestMapping("/meals")
@RestController
class MealWebAdapter(
    private val queryMealsUseCase: QueryMealsUseCase
) {

    @GetMapping("/{date}")
    fun getMeals(
        @PathVariable("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @NotNull mealDate: LocalDate
    ): QueryMealsResponse {
        return queryMealsUseCase.execute(mealDate)
    }
}
