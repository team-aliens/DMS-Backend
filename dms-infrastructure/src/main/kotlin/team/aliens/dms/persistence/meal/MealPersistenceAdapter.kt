package team.aliens.dms.persistence.meal

import org.springframework.stereotype.Component
import team.aliens.dms.domain.meal.model.Meal
import team.aliens.dms.domain.meal.spi.MealPort
import team.aliens.dms.persistence.meal.mapper.MealMapper
import team.aliens.dms.persistence.meal.repository.MealJpaRepository
import java.time.LocalDate
import java.time.YearMonth
import java.util.UUID

@Component
class MealPersistenceAdapter(
    private val mealMapper: MealMapper,
    private val mealRepository: MealJpaRepository
) : MealPort {

    override fun queryAllMealsByMealDateAndSchoolId(mealDate: LocalDate, schoolId: UUID): List<Meal> {
        val month = YearMonth.from(mealDate)

        val firstDay = month.atDay(1)
        val lastDay = month.atEndOfMonth()

        return mealRepository.findBySchoolIdAndIdMealDateBetween(schoolId, firstDay, lastDay).map {
            mealMapper.toDomain(it)!!
        }
    }
}