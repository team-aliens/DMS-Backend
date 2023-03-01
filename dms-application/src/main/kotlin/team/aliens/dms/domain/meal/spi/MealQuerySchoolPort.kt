package team.aliens.dms.domain.meal.spi

import team.aliens.dms.domain.school.model.School

interface MealQuerySchoolPort {

    fun queryAllSchools(): List<School>
}
