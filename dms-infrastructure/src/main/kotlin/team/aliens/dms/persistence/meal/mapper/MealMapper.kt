package team.aliens.dms.persistence.meal.mapper

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component
import team.aliens.dms.domain.meal.model.Meal
import team.aliens.dms.persistence.GenericMapper
import team.aliens.dms.persistence.meal.entity.MealEntity
import team.aliens.dms.persistence.meal.entity.MealEntityId
import team.aliens.dms.persistence.school.repository.SchoolRepository

@Component
class MealMapper(
    private val schoolRepository: SchoolRepository
) : GenericMapper<Meal, MealEntity> {

    override fun toDomain(e: MealEntity): Meal {
        return Meal(
            mealDate = e.id.mealDate,
            schoolId = e.id.schoolId,
            breakfast = e.breakfast,
            lunch = e.lunch,
            dinner = e.dinner
        )
    }

    override fun toEntity(d: Meal): MealEntity {
        val school = schoolRepository.findByIdOrNull(d.schoolId) ?: throw RuntimeException()

        return MealEntity(
            id = MealEntityId(d.mealDate, d.schoolId),
            schoolEntity = school,
            breakfast = d.breakfast,
            lunch = d.lunch,
            dinner = d.dinner
        )
    }
}