package team.aliens.dms.domain.meal.usecase

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.given
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.junit.jupiter.SpringExtension
import team.aliens.dms.common.extension.iterator
import team.aliens.dms.domain.meal.dto.QueryMealsResponse
import team.aliens.dms.domain.meal.model.Meal
import team.aliens.dms.domain.meal.spi.MealQueryStudentPort
import team.aliens.dms.domain.meal.spi.MealSecurityPort
import team.aliens.dms.domain.meal.spi.QueryMealPort
import team.aliens.dms.domain.student.exception.StudentNotFoundException
import team.aliens.dms.domain.student.model.Student
import java.time.LocalDate
import java.time.YearMonth
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

    @BeforeEach
    fun setUp() {
        queryMealsUseCase = QueryMealsUseCase(
            securityPort, queryStudentPort, queryMealPort
        )
    }

    private val mealDate = LocalDate.now()
    private val month = YearMonth.from(mealDate)
    private val firstDay = month.atDay(1)
    private val lastDay = month.atEndOfMonth()
    private val currentUserId = UUID.randomUUID()
    private val schoolId = UUID.randomUUID()

    private val studentStub by lazy {
        Student(
            id = currentUserId,
            roomId = UUID.randomUUID(),
            roomNumber = 123,
            schoolId = schoolId,
            grade = 2,
            classRoom = 3,
            number = 10,
            name = "이름",
            profileImageUrl = "https://~"
        )
    }

    private val mealStub by lazy {
        Meal(
            mealDate = mealDate,
            schoolId = schoolId,
            breakfast = "아침",
            lunch = "점심",
            dinner = "저녁"
        )
    }

    @Test
    fun `급식 조회 성공`() {
        // given
        given(securityPort.getCurrentUserId())
            .willReturn(currentUserId)

        given(queryStudentPort.queryStudentById(currentUserId))
            .willReturn(studentStub)

        given(queryMealPort.queryAllMealsByMonthAndSchoolId(firstDay, lastDay, schoolId))
            .willReturn(listOf(mealStub))

        val mealMap = listOf(mealStub).groupBy { it.mealDate }
        val result = mutableListOf<QueryMealsResponse.MealDetails>()

        for (date in firstDay..lastDay) {
            val meals = mealMap[date]

            if (meals == null) {
                result.add(QueryMealsResponse.MealDetails.emptyOf(date))
            } else {
                meals[0].apply {
                    result.add(QueryMealsResponse.MealDetails.of(this))
                }
            }
        }

        // when
        val response = queryMealsUseCase.execute(mealDate)

        // then
        assertThat(response.meals).isEqualTo(result)
    }

    @Test
    fun `학생 존재하지 않음`() {
        // given
        given(securityPort.getCurrentUserId())
            .willReturn(currentUserId)

        given(queryStudentPort.queryStudentById(currentUserId))
            .willReturn(null)

        // when & then
        assertThrows<StudentNotFoundException> {
            queryMealsUseCase.execute(mealDate)
        }
    }
}