package team.aliens.dms.domain.student.usecase

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.given
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.junit.jupiter.SpringExtension
import team.aliens.dms.domain.student.model.Student
import team.aliens.dms.domain.student.spi.CommandStudentPort
import team.aliens.dms.domain.student.spi.QueryStudentPort
import team.aliens.dms.domain.student.spi.StudentSecurityPort
import team.aliens.dms.domain.user.exception.UserNotFoundException
import java.util.UUID

@ExtendWith(SpringExtension::class)
class UpdateStudentProfileUseCaseTests {

    @MockBean
    private lateinit var securityPort: StudentSecurityPort

    @MockBean
    private lateinit var queryStudentPort: QueryStudentPort

    @MockBean
    private lateinit var commandStudentPort: CommandStudentPort

    private lateinit var updateStudentProfileUseCase: UpdateStudentProfileUseCase

    @BeforeEach
    fun setUp() {
        updateStudentProfileUseCase = UpdateStudentProfileUseCase(
            securityPort, queryStudentPort, commandStudentPort
        )
    }

    private val currentUserId = UUID.randomUUID()
    private val profileImageUrl = "https://~"

    private val studentStub by lazy {
        Student(
            studentId = currentUserId,
            roomNumber = UUID.randomUUID(),
            schoolId = UUID.randomUUID(),
            grade = 1,
            classRoom = 1,
            number = 1,
            name = "이름",
            profileImageUrl = "https://~"
        )
    }

    @Test
    fun `프로필 변경 성공`() {
        // given
        given(securityPort.getCurrentUserId())
            .willReturn(currentUserId)

        given(queryStudentPort.queryStudentById(currentUserId))
            .willReturn(studentStub)

        given(commandStudentPort.saveStudent(studentStub.copy(profileImageUrl = "바뀐 사진 url")))
            .willReturn(studentStub)

        // when & then
        assertDoesNotThrow {
            updateStudentProfileUseCase.execute(profileImageUrl)
        }
    }

    @Test
    fun `유저 미존재`() {
        // given
        given(securityPort.getCurrentUserId())
            .willReturn(currentUserId)

        given(queryStudentPort.queryStudentById(currentUserId))
            .willReturn(null)

        // when & then
        assertThrows<UserNotFoundException> {
            updateStudentProfileUseCase.execute(profileImageUrl)
        }
    }
}