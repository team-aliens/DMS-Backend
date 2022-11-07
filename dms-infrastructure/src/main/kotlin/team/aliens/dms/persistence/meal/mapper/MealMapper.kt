package team.aliens.dms.persistence.meal.mapper

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component
import team.aliens.dms.domain.meal.model.Meal
import team.aliens.dms.persistence.GenericMapper
import team.aliens.dms.persistence.meal.entity.MealJpaEntity
import team.aliens.dms.persistence.meal.entity.MealJpaEntityId
import team.aliens.dms.persistence.school.repository.SchoolJpaRepository

@Component
class MealMapper(
    private val schoolJpaRepository: SchoolJpaRepository
) : GenericMapper<Meal, MealJpaEntity> {

    override fun toDomain(entity: MealJpaEntity?): Meal? {
        return entity?.let {
            Meal(
                mealDate = it.id.mealDate,
                schoolId = it.id.schoolId,
                breakfast = it.breakfast,
                lunch = it.lunch,
                dinner = it.dinner,
                calInfo = it.calInfo
            )
        }
    }

    override fun toEntity(domain: Meal): MealJpaEntity {
        val school = schoolJpaRepository.findByIdOrNull(domain.schoolId)

        return MealJpaEntity(
            id = MealJpaEntityId(domain.mealDate, domain.schoolId),
            school = school,
            breakfast = domain.breakfast,
            lunch = domain.lunch,
            dinner = domain.dinner,
            calInfo = domain.calInfo
        )
    }
}