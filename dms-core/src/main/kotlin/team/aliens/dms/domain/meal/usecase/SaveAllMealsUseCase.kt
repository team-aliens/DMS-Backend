package team.aliens.dms.domain.meal.usecase

import team.aliens.dms.common.annotation.SchedulerUseCase
import team.aliens.dms.domain.meal.model.Meal
import team.aliens.dms.domain.meal.spi.CommandMealPort
import team.aliens.dms.domain.meal.spi.MealFeignClientPort
import team.aliens.dms.domain.meal.spi.MealFeignClientSchoolPort
import team.aliens.dms.domain.school.spi.QuerySchoolPort

@SchedulerUseCase
class SaveAllMealsUseCase(
    private val querySchoolPort: QuerySchoolPort,
    private val commandMealPort: CommandMealPort,
    private val mealFeignClientPort: MealFeignClientPort,
    private val schoolFeignClientPort: MealFeignClientSchoolPort
) {

    fun execute() {
        val meals = mutableListOf<Meal>()

        querySchoolPort.queryAllSchools().map { school ->
            val neisSchool = schoolFeignClientPort.getNeisSchoolInfo(
                schoolName = school.name,
                schoolAddress = school.address
            )

            mealFeignClientPort.getNeisMealServiceDietInfo(
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

        commandMealPort.saveAllMeals(meals)
    }
}
