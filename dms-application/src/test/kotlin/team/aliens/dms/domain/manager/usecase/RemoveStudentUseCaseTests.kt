package team.aliens.dms.domain.manager.usecase

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.given
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.junit.jupiter.SpringExtension
import team.aliens.dms.domain.auth.model.Authority
import team.aliens.dms.domain.manager.exception.ManagerNotFoundException
import team.aliens.dms.domain.manager.spi.ManagerCommandStudentPort
import team.aliens.dms.domain.manager.spi.ManagerCommandUserPort
import team.aliens.dms.domain.manager.spi.ManagerQueryStudentPort
import team.aliens.dms.domain.manager.spi.ManagerQueryUserPort
import team.aliens.dms.domain.manager.spi.ManagerSecurityPort
import team.aliens.dms.domain.school.exception.SchoolMismatchException
import team.aliens.dms.domain.student.exception.StudentNotFoundException
import team.aliens.dms.domain.student.model.Sex
import team.aliens.dms.domain.student.model.Student
import team.aliens.dms.domain.user.exception.UserNotFoundException
import team.aliens.dms.domain.user.model.User
import java.time.LocalDateTime
import java.util.UUID

@ExtendWith(SpringExtension::class)
class RemoveStudentUseCaseTests {

    @MockBean
    private lateinit var securityPort: ManagerSecurityPort

    @MockBean
    private lateinit var queryUserPort: ManagerQueryUserPort

    @MockBean
    private lateinit var queryStudentPort: ManagerQueryStudentPort

    @MockBean
    private lateinit var commandStudentPort: ManagerCommandStudentPort

    @MockBean
    private lateinit var commandUserPort: ManagerCommandUserPort

    private lateinit var removeStudentUseCase: RemoveStudentUseCase

    @BeforeEach
    fun setUp() {
        removeStudentUseCase = RemoveStudentUseCase(
            securityPort, queryUserPort, queryStudentPort, commandStudentPort, commandUserPort
        )
    }

    private val userStub by lazy {
        User(
            id = UUID.randomUUID(),
            schoolId = schoolId,
            accountId = "?????????",
            password = "????????????",
            email = "?????????",
            authority = Authority.MANAGER,
            createdAt = LocalDateTime.now(),
            deletedAt = null
        )
    }

    private val studentStub by lazy {
        Student(
            id = studentId,
            roomId = UUID.randomUUID(),
            roomNumber = 123,
            schoolId = schoolId,
            grade = 2,
            classRoom = 3,
            number = 10,
            name = "??????",
            profileImageUrl = "https://~",
            sex = Sex.FEMALE
        )
    }

    private val managerId = UUID.randomUUID()
    private val studentId = UUID.randomUUID()
    private val schoolId = UUID.randomUUID()

    @Test
    fun `?????? ?????? ??????`() {
        // given
        given(securityPort.getCurrentUserId())
            .willReturn(managerId)

        given(queryUserPort.queryUserById(managerId))
            .willReturn(userStub)

        given(queryStudentPort.queryStudentById(studentId))
            .willReturn(studentStub)

        given(queryUserPort.queryUserById(studentStub.id))
            .willReturn(userStub)

        given(commandUserPort.saveUser(userStub.copy(deletedAt = LocalDateTime.now())))
            .willReturn(userStub)

        // when & then
        assertDoesNotThrow {
            removeStudentUseCase.execute(studentId)
        }
    }

    @Test
    fun `????????? ?????????`() {
        // given
        given(securityPort.getCurrentUserId())
            .willReturn(managerId)

        given(queryUserPort.queryUserById(managerId))
            .willReturn(null)

        // when & then
        assertThrows<ManagerNotFoundException> {
            removeStudentUseCase.execute(studentId)
        }
    }

    @Test
    fun `?????? ?????????`() {
        // given
        given(securityPort.getCurrentUserId())
            .willReturn(managerId)

        given(queryUserPort.queryUserById(managerId))
            .willReturn(userStub)

        given(queryStudentPort.queryStudentById(studentId))
            .willReturn(null)

        // when & then
        assertThrows<StudentNotFoundException> {
            removeStudentUseCase.execute(studentId)
        }
    }

    @Test
    fun `?????? ?????? ?????????`() {
        // given
        given(securityPort.getCurrentUserId())
            .willReturn(managerId)

        given(queryUserPort.queryUserById(managerId))
            .willReturn(userStub)

        given(queryStudentPort.queryStudentById(studentId))
            .willReturn(studentStub)

        given(queryUserPort.queryUserById(studentStub.id))
            .willReturn(null)

        // when & then
        assertThrows<UserNotFoundException> {
            removeStudentUseCase.execute(studentId)
        }
    }

    @Test
    fun `?????? ?????????`() {
        // given
        given(securityPort.getCurrentUserId())
            .willReturn(managerId)

        given(queryUserPort.queryUserById(managerId))
            .willReturn(userStub.copy(schoolId = UUID.randomUUID()))

        given(queryStudentPort.queryStudentById(studentId))
            .willReturn(studentStub)

        given(queryUserPort.queryUserById(studentStub.id))
            .willReturn(userStub)

        // when & then
        assertThrows<SchoolMismatchException> {
            removeStudentUseCase.execute(studentId)
        }
    }
}