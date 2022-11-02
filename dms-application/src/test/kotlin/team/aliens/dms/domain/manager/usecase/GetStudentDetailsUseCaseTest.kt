package team.aliens.dms.domain.manager.usecase

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.given
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.junit.jupiter.SpringExtension
import team.aliens.dms.common.util.GCNToStringUtil
import team.aliens.dms.domain.manager.dto.GetStudentDetailsResponse
import team.aliens.dms.domain.manager.spi.ManagerQueryPointHistoryPort
import team.aliens.dms.domain.manager.spi.ManagerQueryStudentPort
import team.aliens.dms.domain.manager.spi.ManagerQueryUserPort
import team.aliens.dms.domain.student.exception.StudentNotFoundException
import team.aliens.dms.domain.student.model.Student
import team.aliens.dms.domain.user.exception.UserNotFoundException
import team.aliens.dms.domain.user.model.User
import java.util.UUID

@ExtendWith(SpringExtension::class)
class GetStudentDetailsUseCaseTest {

    @MockBean
    private lateinit var queryUserPort: ManagerQueryUserPort

    @MockBean
    private lateinit var queryStudentPort: ManagerQueryStudentPort

    @MockBean
    private lateinit var queryPointHistoryPort: ManagerQueryPointHistoryPort

    @MockBean
    private lateinit var gcnToStringUtil: GCNToStringUtil

    private lateinit var getStudentDetailsUseCase: GetStudentDetailsUseCase

    @BeforeEach
    fun setUp() {
        getStudentDetailsUseCase = GetStudentDetailsUseCase(
            queryUserPort,
            queryStudentPort,
            queryPointHistoryPort,
            gcnToStringUtil
        )
    }

    private val id = UUID.randomUUID()
    private val bonusPoint = 11
    private val minusPoint = 23

    private val userStub by lazy {
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

    private val studentStub by lazy {
        Student(
            studentId = id,
            roomNumber = 216,
            schoolId = id,
            grade = 2,
            classRoom = 1,
            number = 20
        )
    }

    private val responseStub = GetStudentDetailsResponse(
        name = userStub.name,
        gcn = studentStub.grade.toString().plus(studentStub.classRoom).plus(studentStub.number),
        profileImageUrl = userStub.profileImageUrl!!,
        bonusPoint = bonusPoint,
        minusPoint = minusPoint,
        roomNumber = studentStub.roomNumber,
        roomMates = emptyList()
    )

    @Test
    fun `학생 상세조회 성공`() {
        // given
        given(queryUserPort.queryUserById(id))
            .willReturn(userStub)
        given(queryStudentPort.queryStudentById(id))
            .willReturn(studentStub)
        given(gcnToStringUtil.gcnToString(studentStub.grade, studentStub.classRoom, studentStub.number))
            .willReturn(
                studentStub.grade.toString()
                    .plus(studentStub.classRoom.toString())
                    .plus(studentStub.number.toString())
            )
        given(queryPointHistoryPort.queryTotalBonusPoint(studentStub.studentId))
            .willReturn(bonusPoint)
        given(queryPointHistoryPort.queryTotalMinusPoint(studentStub.studentId))
            .willReturn(minusPoint)
        given(queryUserPort.queryUserByRoomNumberAndSchoolId(studentStub.roomNumber, studentStub.schoolId))
            .willReturn(emptyList())

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
            .willReturn(userStub)
        given(queryStudentPort.queryStudentById(id))
            .willReturn(null)

        // when & then
        assertThrows<StudentNotFoundException> {
            getStudentDetailsUseCase.execute(id)
        }
    }
}