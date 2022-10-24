package team.aliens.dms.domain.meal.usecase

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.given
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.junit.jupiter.SpringExtension
import team.aliens.dms.domain.meal.exception.MealNotFoundException
import team.aliens.dms.domain.meal.model.Meal
import team.aliens.dms.domain.meal.spi.MealSecurityPort
import team.aliens.dms.domain.meal.spi.MealQueryUserPort
import team.aliens.dms.domain.meal.spi.QueryMealPort
import team.aliens.dms.domain.student.exception.StudentNotFoundException
import team.aliens.dms.domain.user.model.User
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.UUID

@ExtendWith(SpringExtension::class)
class QueryMealsUseCaseTest {

    @MockBean
    private lateinit var securityPort: MealSecurityPort

    @MockBean
    private lateinit var queryUserPort: MealQueryUserPort

    @MockBean
    private lateinit var queryMealPort: QueryMealPort

    private lateinit var queryMealsUseCase: QueryMealsUseCase

    private val mealDate = LocalDate.now()

    private val userId = UUID.randomUUID()

    private val schoolId = UUID.randomUUID()

    private val user by lazy {
        User(
            id = userId,
            schoolId = schoolId,
            accountId = "이정윤계정아이디",
            password = "이정윤비밀번호",
            email = "이정윤이메일@naver.com",
            name = "이정윤",
            profileImageUrl = "http://~~",
            createdAt = LocalDateTime.now(),
            deletedAt = null
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

    private val mealBreakfastError by lazy {
        Meal(
            mealDate = LocalDate.now(),
            schoolId = schoolId,
            breakfast = null,
            lunch = "점심",
            dinner = "저녁"
        )
    }

    private val mealLunchError by lazy {
        Meal(
            mealDate = LocalDate.now(),
            schoolId = schoolId,
            breakfast = "아침",
            lunch = null,
            dinner = "저녁"
        )
    }

    private val mealDinnerError by lazy {
        Meal(
            mealDate = LocalDate.now(),
            schoolId = schoolId,
            breakfast = "아침",
            lunch = "점심",
            dinner = null
        )
    }

    @BeforeEach
    fun setUp() {
        queryMealsUseCase = QueryMealsUseCase(securityPort, queryUserPort, queryMealPort)
    }

    @Test
    fun `급식 조회 성공`() {
        given(securityPort.getCurrentUserId())
            .willReturn(userId)

        given(queryUserPort.queryByUserId(userId))
            .willReturn(user)

        given(queryMealPort.queryAllByMealDateAndSchoolId(mealDate, schoolId))
            .willReturn(listOf(meal))

        val response = queryMealsUseCase.execute(mealDate)

        assertThat(response)
    }

    @Test
    fun `학생 존재하지 않음`() {
        given(securityPort.getCurrentUserId())
            .willReturn(userId)

        given(queryUserPort.queryByUserId(userId))
            .willReturn(null)

        assertThrows<StudentNotFoundException> {
            queryMealsUseCase.execute(mealDate)
        }
    }

    @Test
    fun `아침 존재하지 않음`() {
        given(securityPort.getCurrentUserId())
            .willReturn(userId)

        given(queryUserPort.queryByUserId(userId))
            .willReturn(user)

        given(queryMealPort.queryAllByMealDateAndSchoolId(mealDate, schoolId))
            .willReturn(listOf(mealBreakfastError))

        assertThrows<MealNotFoundException> {
            queryMealsUseCase.execute(mealDate)
        }
    }

    @Test
    fun `점심 존재하지 않음`() {
        given(securityPort.getCurrentUserId())
            .willReturn(userId)

        given(queryUserPort.queryByUserId(userId))
            .willReturn(user)

        given(queryMealPort.queryAllByMealDateAndSchoolId(mealDate, schoolId))
            .willReturn(listOf(mealLunchError))

        assertThrows<MealNotFoundException> {
            queryMealsUseCase.execute(mealDate)
        }
    }

    @Test
    fun `저녁 존재하지 않음`() {
        given(securityPort.getCurrentUserId())
            .willReturn(userId)

        given(queryUserPort.queryByUserId(userId))
            .willReturn(user)

        given(queryMealPort.queryAllByMealDateAndSchoolId(mealDate, schoolId))
            .willReturn(listOf(mealDinnerError))

        assertThrows<MealNotFoundException> {
            queryMealsUseCase.execute(mealDate)
        }
    }
}