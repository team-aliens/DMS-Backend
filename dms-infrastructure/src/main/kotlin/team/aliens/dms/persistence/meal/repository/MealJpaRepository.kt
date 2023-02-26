package team.aliens.dms.persistence.meal.repository

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import team.aliens.dms.persistence.meal.entity.MealJpaEntity
import team.aliens.dms.persistence.meal.entity.MealJpaEntityId
import java.time.LocalDate
import java.util.UUID

@Repository
interface MealJpaRepository : CrudRepository<MealJpaEntity, MealJpaEntityId> {

    fun findBySchoolIdAndIdMealDateBetween(schoolId: UUID, startAt: LocalDate, endAt: LocalDate): List<MealJpaEntity>
}
