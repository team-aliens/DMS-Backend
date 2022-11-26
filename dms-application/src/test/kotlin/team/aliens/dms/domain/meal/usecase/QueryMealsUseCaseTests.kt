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
import team.aliens.dms.domain.meal.dto.QueryMealsResponse.MealDetails
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

    @BeforeEach
    fun setUp() {
        queryMealsUseCase = QueryMealsUseCase(
            securityPort, queryStudentPort, queryMealPort
        )
    }

    private val mealDate = LocalDate.now()
    private val firstDay = LocalDate.now()
    private val lastDay = LocalDate.now()
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

    private val breakfastNullMealStub by lazy {
        Meal(
            mealDate = mealDate,
            schoolId = schoolId,
            breakfast = null,
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

        // when
        val response = queryMealsUseCase.execute(mealDate)

        // then
        assertThat(response).isNotNull
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