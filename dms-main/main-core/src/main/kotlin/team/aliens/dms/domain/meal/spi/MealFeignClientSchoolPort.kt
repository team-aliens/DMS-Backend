package team.aliens.dms.domain.meal.spi

import team.aliens.dms.domain.school.dto.NeisSchoolResponse

interface MealFeignClientSchoolPort {

    fun getNeisSchoolInfo(schoolName: String, schoolAddress: String): NeisSchoolResponse
}
