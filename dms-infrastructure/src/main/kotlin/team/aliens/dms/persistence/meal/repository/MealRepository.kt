package team.aliens.dms.persistence.meal.repository

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import team.aliens.dms.persistence.meal.entity.MealJpaEntity
import team.aliens.dms.persistence.meal.entity.MealJpaEntityId

@Repository
interface MealRepository : CrudRepository<MealJpaEntity, MealJpaEntityId> {
}