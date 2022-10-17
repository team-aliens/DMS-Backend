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
        val school = e.schoolEntity?.let {
            schoolRepository.findByIdOrNull(it.id)
        } ?: throw RuntimeException()

        return Meal(
            mealDate = e.id.mealDate,
            schoolId = school.id,
            breakfast = e.breakfast,
            lunch = e.lunch,
            dinner = e.dinner
        )
    }

    override fun toEntity(d: Meal): MealEntity {
        val school = schoolRepository.findByIdOrNull(d.schoolId) ?: throw RuntimeException()

        return MealEntity(
            id = MealEntityId(
                mealDate = d.mealDate,
                schoolId = school.id
            ),
            breakfast = d.breakfast,
            lunch = d.lunch,
            dinner = d.dinner,
            schoolEntity = school
        )
    }
}