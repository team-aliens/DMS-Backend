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

@ExtendWith(SpringExtension::class)
class QueryStudentsUseCaseTests {

    @MockBean
    private lateinit var queryStudentPort: ManagerQueryStudentPort

    private lateinit var queryStudentsUseCase: QueryStudentsUseCase

    @BeforeEach
    fun setUp() {
        queryStudentsUseCase = QueryStudentsUseCase(
            queryStudentPort
        )
    }

    private val id = UUID.randomUUID()
    private val name = "name"
    private val sort = Sort.GCN

    private val studentStub by lazy {
        Student(
            id = id,
            roomId = UUID.randomUUID(),
            roomNumber = 216,
            schoolId = id,
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
        given(queryStudentPort.queryStudentsByNameAndSort(name, sort))
            .willReturn(listOf(studentStub))

        // when
        val response = queryStudentsUseCase.execute(name, sort)

        // then
        assertThat(response).isNotNull
    }
}