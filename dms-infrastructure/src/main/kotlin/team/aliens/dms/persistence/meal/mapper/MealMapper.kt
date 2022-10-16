package team.aliens.dms.persistence.meal.mapper

import org.mapstruct.Mapper
import org.mapstruct.Mapping
import team.aliens.dms.domain.meal.model.Meal
import team.aliens.dms.persistence.GenericMapper
import team.aliens.dms.persistence.meal.entity.MealEntity

@Mapper
interface MealMapper : GenericMapper<Meal, MealEntity> {

    @Mapping(source = "id.mealDate", target = "mealDate")
    @Mapping(source = "id.schoolId", target = "schoolId")
    override fun toDomain(e: MealEntity): Meal

    @Mapping(source = "schoolId", target = "schoolEntity.id")
    override fun toEntity(d: Meal): MealEntity
}