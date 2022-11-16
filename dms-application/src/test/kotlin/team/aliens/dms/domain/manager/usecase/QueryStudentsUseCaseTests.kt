package team.aliens.dms.domain.manager.usecase

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.given
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.junit.jupiter.SpringExtension
import team.aliens.dms.domain.manager.dto.QueryStudentListResponse
import team.aliens.dms.domain.manager.dto.Sort
import team.aliens.dms.domain.manager.spi.ManagerQueryStudentPort
import team.aliens.dms.domain.manager.spi.ManagerQueryUserPort
import team.aliens.dms.domain.student.exception.StudentNotFoundException
import team.aliens.dms.domain.student.model.Student
import team.aliens.dms.domain.user.model.User
import java.util.UUID

@ExtendWith(SpringExtension::class)
class QueryStudentsUseCaseTests {

    @MockBean
    private lateinit var queryUserPort: ManagerQueryUserPort

    @MockBean
    private lateinit var queryStudentPort: ManagerQueryStudentPort

    private lateinit var queryStudentsUseCase: QueryStudentsUseCase

    @BeforeEach
    fun setUp() {
        queryStudentsUseCase = QueryStudentsUseCase(
            queryUserPort, queryStudentPort
        )
    }

    private val id = UUID.randomUUID()
    private val name = "name"
    private val sort = Sort.GCN

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

    private val responseStub by lazy {
        QueryStudentListResponse(
            listOf(
                QueryStudentListResponse.StudentElement(
                    id = id,
                    name = userStub.name,
                    gcn = "2120",
                    roomNumber = studentStub.roomNumber,
                    profileImageUrl = userStub.profileImageUrl!!
                )
            )
        )
    }

    @Test
    fun `학생 목록 조회 성공`() {
        // given
        given(queryUserPort.queryUserByNameAndSort(name, sort))
            .willReturn(listOf(userStub))

        given(queryStudentPort.queryStudentById(id))
            .willReturn(studentStub)

        // when
        val response = queryStudentsUseCase.execute(name, sort)

        // then
        assertThat(response).isEqualTo(responseStub)
    }

    @Test
    fun `학생을 찾을 수 없음`() {
        // given
        given(queryUserPort.queryUserByNameAndSort(name, sort))
            .willReturn(listOf(userStub))

        given(queryStudentPort.queryStudentById(id))
            .willReturn(null)

        // when & then
        assertThrows<StudentNotFoundException> {
            queryStudentsUseCase.execute(name, sort)
        }
    }
}