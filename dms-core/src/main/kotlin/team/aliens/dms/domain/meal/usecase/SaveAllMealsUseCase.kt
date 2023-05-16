package team.aliens.dms.domain.meal.usecase

import team.aliens.dms.common.annotation.SchedulerUseCase
import team.aliens.dms.common.service.feign.FeignService
import team.aliens.dms.domain.meal.model.Meal
import team.aliens.dms.domain.meal.service.MealService
import team.aliens.dms.domain.school.service.SchoolService

@SchedulerUseCase
class SaveAllMealsUseCase(
    private val schoolService: SchoolService,
    private val mealService: MealService,
    private val feignService: FeignService
) {

    fun execute() {
        val meals = mutableListOf<Meal>()

        schoolService.getAllSchools().map { school ->
            val neisSchool = feignService.getNeisSchoolInfo(
                schoolName = school.name,
                schoolAddress = school.address
            )

            feignService.getNeisMealServiceDietInfo(
                sdSchoolCode = neisSchool.sdSchoolCode,
                regionCode = neisSchool.regionCode
            ).mealServiceDietInfos.map { mealDietInfo ->
                val meal = Meal(
                    mealDate = mealDietInfo.mealDate,
                    schoolId = school.id,
                    breakfast = mealDietInfo.breakfast,
                    lunch = mealDietInfo.lunch,
                    dinner = mealDietInfo.dinner
                )

                meals.add(meal)
            }
        }

        mealService.saveAllMeals(meals)
    }
}
