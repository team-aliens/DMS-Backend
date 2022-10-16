package team.aliens.dms.persistence.meal.entity.repository

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import team.aliens.dms.persistence.meal.entity.MealEntity
import team.aliens.dms.persistence.meal.entity.MealEntityId

@Repository
interface MealRepository : CrudRepository<MealEntity, MealEntityId> {
}