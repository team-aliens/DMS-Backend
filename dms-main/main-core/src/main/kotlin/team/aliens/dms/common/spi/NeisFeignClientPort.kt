package team.aliens.dms.common.spi

import team.aliens.dms.domain.meal.spi.MealFeignClientPort
import team.aliens.dms.domain.meal.spi.MealFeignClientSchoolPort

interface NeisFeignClientPort :
    MealFeignClientSchoolPort,
    MealFeignClientPort
