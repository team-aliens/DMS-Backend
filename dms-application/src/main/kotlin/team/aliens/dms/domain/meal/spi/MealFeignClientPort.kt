package team.aliens.dms.domain.meal.spi

import team.aliens.dms.domain.meal.dto.NeisMealServiceDietInfosResponse

interface MealFeignClientPort {

    fun getNeisMealServiceDietInfo(sdSchoolCode: String, regionCode: String): NeisMealServiceDietInfosResponse
}
