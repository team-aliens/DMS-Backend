package team.aliens.dms.domain.manager.usecase

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.given
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.junit.jupiter.SpringExtension
import team.aliens.dms.domain.manager.dto.Sort
import team.aliens.dms.domain.manager.spi.ManagerQueryStudentPort
import team.aliens.dms.domain.student.model.Student
import java.util.UUID
import org.junit.jupiter.api.assertThrows
import team.aliens.dms.domain.manager.exception.ManagerNotFoundException
import team.aliens.dms.domain.manager.model.Manager
import team.aliens.dms.domain.manager.spi.ManagerSecurityPort
import team.aliens.dms.domain.manager.spi.QueryManagerPort

@ExtendWith(SpringExtension::class)
class QueryStudentsUseCaseTests {

    @MockBean
    private lateinit var securityPort: ManagerSecurityPort

    @MockBean
    private lateinit var queryManagerPort: QueryManagerPort

    @MockBean
    private lateinit var queryStudentPort: ManagerQueryStudentPort


    private lateinit var queryStudentsUseCase: QueryStudentsUseCase

    @BeforeEach
    fun setUp() {
        queryStudentsUseCase = QueryStudentsUseCase(
            securityPort, queryManagerPort, queryStudentPort
        )
    }

    private val studentId = UUID.randomUUID()
    private val currentUserId = UUID.randomUUID()
    private val schoolId = UUID.randomUUID()
    private val name = "name"
    private val sort = Sort.GCN

    private val managerStub by lazy {
        Manager(
            id = currentUserId,
            schoolId = schoolId,
            name = "관리자 이름",
            profileImageUrl = "https://~~"
        )
    }

    private val studentStub by lazy {
        Student(
            id = studentId,
            roomId = UUID.randomUUID(),
            roomNumber = 216,
            schoolId = schoolId,
            grade = 2,
            classRoom = 1,
            number = 20,
            name = name,
            profileImageUrl = "profile image url"
        )
    }

    @Test
    fun `학생 목록 조회 성공`() {
        // given
        given(securityPort.getCurrentUserId())
            .willReturn(currentUserId)

        given(queryManagerPort.queryManagerById(currentUserId))
            .willReturn(managerStub)

        given(queryStudentPort.queryStudentsByNameAndSort(name, sort))
            .willReturn(listOf(studentStub))

        // when
        val response = queryStudentsUseCase.execute(name, sort)

        // then
        assertThat(response).isNotNull
    }

    @Test
    fun `관리자 미존재`() {
        // given
        given(securityPort.getCurrentUserId())
            .willReturn(currentUserId)

        given(queryManagerPort.queryManagerById(currentUserId))
            .willReturn(null)

        assertThrows<ManagerNotFoundException> {
            queryStudentsUseCase.execute(name, sort)
        }
    }
}