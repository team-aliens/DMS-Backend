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
import team.aliens.dms.domain.manager.spi.ManagerQueryPointPort
import team.aliens.dms.domain.manager.spi.ManagerQueryStudentPort
import team.aliens.dms.domain.student.exception.StudentNotFoundException
import team.aliens.dms.domain.student.model.Student
import java.util.UUID

@ExtendWith(SpringExtension::class)
class QueryStudentDetailsUseCaseTests {

    @MockBean
    private lateinit var queryStudentPort: ManagerQueryStudentPort

    @MockBean
    private lateinit var queryPointPort: ManagerQueryPointPort

    private lateinit var queryStudentDetailsUseCase: QueryStudentDetailsUseCase

    @BeforeEach
    fun setUp() {
        queryStudentDetailsUseCase = QueryStudentDetailsUseCase(
            queryStudentPort, queryPointPort
        )
    }

    private val id = UUID.randomUUID()
    private val bonusPoint = 11
    private val minusPoint = 23

    private val studentStub by lazy {
        Student(
            id = id,
            roomId = UUID.randomUUID(),
            roomNumber = 216,
            schoolId = id,
            grade = 2,
            classRoom = 1,
            number = 20,
            name = "김범진",
            profileImageUrl = "profile image url"
        )
    }

    private val responseStub by lazy {
        GetStudentDetailsResponse(
            name = studentStub.name,
            gcn = studentStub.gcn,
            profileImageUrl = studentStub.profileImageUrl ?: Student.PROFILE_IMAGE,
            bonusPoint = bonusPoint,
            minusPoint = minusPoint,
            roomNumber = studentStub.roomNumber,
            roomMates = listOf(
                GetStudentDetailsResponse.RoomMate(
                    id = studentStub.id,
                    name = studentStub.name,
                    profileImageUrl = studentStub.profileImageUrl ?: Student.PROFILE_IMAGE
                )
            )
        )
    }

    @Test
    fun `학생 상세조회 성공`() {
        // given
        given(queryStudentPort.queryStudentById(id))
            .willReturn(studentStub)

        given(queryPointPort.queryTotalBonusPoint(studentStub.id))
            .willReturn(bonusPoint)

        given(queryPointPort.queryTotalMinusPoint(studentStub.id))
            .willReturn(minusPoint)

        given(queryStudentPort.queryUserByRoomNumberAndSchoolId(studentStub.roomNumber, studentStub.schoolId))
            .willReturn(listOf(studentStub))

        // when
        val response = queryStudentDetailsUseCase.execute(id)

        // then
        assertThat(response).isEqualTo(responseStub)
    }

    @Test
    fun `학생이 아니거나 찾을 수 없음`() {
        // given
        given(queryStudentPort.queryStudentById(id))
            .willReturn(null)

        // when & then
        assertThrows<StudentNotFoundException> {
            queryStudentDetailsUseCase.execute(id)
        }
    }
}