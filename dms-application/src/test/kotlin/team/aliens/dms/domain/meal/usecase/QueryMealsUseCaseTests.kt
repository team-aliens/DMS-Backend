package team.aliens.dms.domain.meal.usecase

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.given
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.junit.jupiter.SpringExtension
import team.aliens.dms.domain.meal.dto.QueryMealsResponse
import team.aliens.dms.domain.meal.model.Meal
import team.aliens.dms.domain.meal.spi.MealQueryStudentPort
import team.aliens.dms.domain.meal.spi.MealSecurityPort
import team.aliens.dms.domain.meal.spi.QueryMealPort
import team.aliens.dms.domain.student.exception.StudentNotFoundException
import team.aliens.dms.domain.student.model.Student
import java.time.LocalDate
import java.util.UUID

@ExtendWith(SpringExtension::class)
class QueryMealsUseCaseTests {

    @MockBean
    private lateinit var securityPort: MealSecurityPort

    @MockBean
    private lateinit var queryStudentPort: MealQueryStudentPort

    @MockBean
    private lateinit var queryMealPort: QueryMealPort

    private lateinit var queryMealsUseCase: QueryMealsUseCase

    private val mealDate = LocalDate.now()

    private val userId = UUID.randomUUID()

    private val schoolId = UUID.randomUUID()

    private val student by lazy {
        Student(
            studentId = userId,
            roomNumber = 318,
            schoolId = schoolId,
            grade = 2,
            classRoom = 3,
            number = 10
        )
    }

    private val meal by lazy {
        Meal(
            mealDate = LocalDate.now(),
            schoolId = schoolId,
            breakfast = "아침",
            lunch = "점심",
            dinner = "저녁"
        )
    }

    private val breakfastNullMeal by lazy {
        Meal(
            mealDate = LocalDate.now(),
            schoolId = schoolId,
            breakfast = null,
            lunch = "점심",
            dinner = "저녁"
        )
    }

    @BeforeEach
    fun setUp() {
        queryMealsUseCase = QueryMealsUseCase(securityPort, queryStudentPort, queryMealPort)
    }

    @Test
    fun `급식 조회 성공`() {
        val mealDetails by lazy {
            QueryMealsResponse.MealDetail(
                date = mealDate,
                breakfast = listOf(meal.breakfast),
                lunch = listOf(meal.lunch),
                dinner = listOf(meal.dinner)
            )
        }

        val result by lazy {
            QueryMealsResponse(
                meals = listOf(mealDetails)
            )
        }

        given(securityPort.getCurrentUserId())
            .willReturn(userId)

        given(queryStudentPort.queryByUserId(userId))
            .willReturn(student)

        given(queryMealPort.queryAllMealsByMealDateAndSchoolId(mealDate, schoolId))
            .willReturn(listOf(meal))

        val response = queryMealsUseCase.execute(mealDate)

        assertThat(response).isEqualTo(result)
    }

    @Test
    fun `학생 존재하지 않음`() {
        given(securityPort.getCurrentUserId())
            .willReturn(userId)

        given(queryStudentPort.queryByUserId(userId))
            .willReturn(null)

        assertThrows<StudentNotFoundException> {
            queryMealsUseCase.execute(mealDate)
        }
    }

    @Test
    fun `급식 값 없음`() {
        val breakfastNullMealDetails by lazy {
            QueryMealsResponse.MealDetail(
                date = mealDate,
                breakfast = listOf(),
                lunch = listOf(meal.lunch),
                dinner = listOf(meal.dinner)
            )
        }

        val result by lazy {
            QueryMealsResponse(
                meals = listOf(breakfastNullMealDetails)
            )
        }

        given(securityPort.getCurrentUserId())
            .willReturn(userId)

        given(queryStudentPort.queryByUserId(userId))
            .willReturn(student)

        given(queryMealPort.queryAllMealsByMealDateAndSchoolId(mealDate, schoolId))
            .willReturn(listOf(breakfastNullMeal))

        val response = queryMealsUseCase.execute(mealDate)

        assertThat(response).isEqualTo(result)
    }
}