package team.aliens.dms.persistence.meal.mapper

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component
import team.aliens.dms.domain.meal.model.Meal
import team.aliens.dms.domain.school.exception.SchoolNotFoundException
import team.aliens.dms.persistence.GenericMapper
import team.aliens.dms.persistence.meal.entity.MealJpaEntity
import team.aliens.dms.persistence.meal.entity.MealJpaEntityId
import team.aliens.dms.persistence.school.repository.SchoolRepository

@Component
class MealMapper(
    private val schoolRepository: SchoolRepository
) : GenericMapper<Meal, MealJpaEntity> {

    override fun toDomain(e: MealJpaEntity): Meal {
        return Meal(
            mealDate = e.id.mealDate,
            schoolId = e.id.schoolId,
            breakfast = e.breakfast,
            lunch = e.lunch,
            dinner = e.dinner
        )
    }

    override fun toEntity(d: Meal): MealJpaEntity {
        val school = schoolRepository.findByIdOrNull(d.schoolId) ?: throw SchoolNotFoundException

        return MealJpaEntity(
            id = MealJpaEntityId(d.mealDate, d.schoolId),
            schoolJpaEntity = school,
            breakfast = d.breakfast,
            lunch = d.lunch,
            dinner = d.dinner
        )
    }
}