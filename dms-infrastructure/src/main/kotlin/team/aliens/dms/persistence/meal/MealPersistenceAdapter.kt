package team.aliens.dms.persistence.meal

import org.springframework.stereotype.Component
import team.aliens.dms.domain.meal.model.Meal
import team.aliens.dms.domain.meal.spi.MealPort
import team.aliens.dms.persistence.meal.mapper.MealMapper
import team.aliens.dms.persistence.meal.repository.MealJpaRepository
import java.time.LocalDate
import java.util.UUID

@Component
class MealPersistenceAdapter(
    private val mealMapper: MealMapper,
    private val mealRepository: MealJpaRepository
) : MealPort {

    override fun queryAllMealsByMonthAndSchoolId(
        firstDay: LocalDate,
        lastDay: LocalDate,
        schoolId: UUID
    ): List<Meal> {
        return mealRepository.findBySchoolIdAndIdMealDateBetween(schoolId, firstDay, lastDay).map {
            mealMapper.toDomain(it)!!
        }
    }

    override fun saveAllMeals(meals: List<Meal>) {
        mealRepository.saveAll(
            meals.map {
                mealMapper.toEntity(it)
            }
        )
    }
}
