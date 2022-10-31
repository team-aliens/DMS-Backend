package team.aliens.dms.domain.manager.usecase

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.given
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.junit.jupiter.SpringExtension
import team.aliens.dms.domain.manager.dto.GetStudentDetailsResponse
import team.aliens.dms.domain.manager.spi.ManagerQueryPointHistoryPort
import team.aliens.dms.domain.manager.spi.ManagerQueryStudentPort
import team.aliens.dms.domain.manager.spi.ManagerQueryUserPort
import team.aliens.dms.domain.student.exception.StudentNotFoundException
import team.aliens.dms.domain.student.model.Student
import team.aliens.dms.domain.user.exception.UserNotFoundException
import team.aliens.dms.domain.user.model.User
import java.util.*

@ExtendWith(SpringExtension::class)
class GetStudentDetailsUseCaseTest {

    @MockBean
    private lateinit var queryUserPort: ManagerQueryUserPort

    @MockBean
    private lateinit var queryStudentPort: ManagerQueryStudentPort

    @MockBean
    private lateinit var queryPointHistoryPort: ManagerQueryPointHistoryPort

    private lateinit var getStudentDetailsUseCase: GetStudentDetailsUseCase

    @BeforeEach
    fun setUp() {
        getStudentDetailsUseCase = GetStudentDetailsUseCase(
            queryUserPort,
            queryStudentPort,
            queryPointHistoryPort
        )
    }

    private val id = UUID.randomUUID()

    private val user by lazy {
        User(
            id = id,
            schoolId = id,
            accountId = "accountId",
            password = "password",
            email = "hc@dsm.hs.kr",
            name = "마구니",
            profileImageUrl = "https://~~",
            createdAt = null,
            deletedAt = null
        )
    }

    private val student by lazy {
        Student(
            studentId = id,
            roomNumber = 216,
            schoolId = id,
            grade = 2,
            classRoom = 1,
            number= 20
        )
    }

    @Test
    fun `학생 상세조회 성공`() {
        val responseStub = GetStudentDetailsResponse(
            name = user.name,
            gcn = student.grade.toString().plus(student.classRoom).plus(student.number),
            profileImageUrl = user.profileImageUrl!!,
            bonusPoint = 11,
            minusPoint = 24,
            roomNumber = student.roomNumber,
            roomMates = emptyList()
        )

        // given
        given(queryUserPort.queryUserById(id))
            .willReturn(user)
        given(queryStudentPort.queryByUserId(id))
            .willReturn(student)
        given(queryUserPort.queryUserByRoomNumberAndSchoolId(student.roomNumber, student.schoolId))
            .willReturn(emptyList())
        given(queryPointHistoryPort.queryPointScore(id, true))
            .willReturn(11)
        given((queryPointHistoryPort.queryPointScore(id, false)))
            .willReturn(24)

        // when
        val response = getStudentDetailsUseCase.execute(id)

        // then
        assertThat(response).isEqualTo(responseStub)
    }

    @Test
    fun `유저를 찾을 수 없음`() {
        // given
        given(queryUserPort.queryUserById(id))
            .willReturn(null)

        // when & then
        assertThrows<UserNotFoundException> {
            getStudentDetailsUseCase.execute(id)
        }
    }

    @Test
    fun `학생이 아니거나 찾을 수 없음`() {
        // given
        given(queryUserPort.queryUserById(id))
            .willReturn(user)
        given(queryStudentPort.queryByUserId(id))
            .willReturn(null)

        // when & then
        assertThrows<StudentNotFoundException> {
            getStudentDetailsUseCase.execute(id)
        }
    }
}