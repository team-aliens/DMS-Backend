package team.aliens.dms.persistence.meal

import org.springframework.stereotype.Component
import team.aliens.dms.domain.meal.model.Meal
import team.aliens.dms.domain.meal.spi.MealPort
import team.aliens.dms.persistence.meal.entity.MealJpaEntityId
import team.aliens.dms.persistence.meal.mapper.MealMapper
import team.aliens.dms.persistence.meal.repository.MealJpaRepository
import java.time.LocalDate
import java.util.UUID

@Component
class MealPersistenceAdapter(
    private val mealMapper: MealMapper,
    private val mealRepository: MealJpaRepository
) : MealPort {

    override fun queryAllMealsByMealDateAndSchoolId(mealDate: LocalDate, schoolId: UUID): List<Meal> {
        val mealId = MealJpaEntityId(mealDate, schoolId)

        return mealRepository.findAllById(mealId).map {
            mealMapper.toDomain(it)!!
        }
    }
}