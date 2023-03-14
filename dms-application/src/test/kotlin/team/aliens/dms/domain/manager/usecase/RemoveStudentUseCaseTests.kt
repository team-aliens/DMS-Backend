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
import team.aliens.dms.domain.manager.spi.ManagerCommandUserPort
import team.aliens.dms.domain.manager.spi.ManagerQueryStudentPort
import team.aliens.dms.domain.manager.spi.ManagerQueryUserPort
import team.aliens.dms.domain.manager.spi.ManagerSecurityPort
import team.aliens.dms.domain.school.exception.SchoolMismatchException
import team.aliens.dms.domain.student.exception.StudentNotFoundException
import team.aliens.dms.domain.student.model.Sex
import team.aliens.dms.domain.student.model.Student
import team.aliens.dms.domain.student.spi.CommandStudentPort
import team.aliens.dms.domain.student.spi.StudentCommandRemainStatusPort
import team.aliens.dms.domain.student.spi.StudentCommandStudyRoomPort
import team.aliens.dms.domain.student.spi.StudentQueryStudyRoomPort
import team.aliens.dms.domain.studyroom.model.Seat
import team.aliens.dms.domain.studyroom.model.SeatStatus
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
    private lateinit var commandRemainStatusPort: StudentCommandRemainStatusPort

    @MockBean
    private lateinit var queryStudyRoomPort: StudentQueryStudyRoomPort

    @MockBean
    private lateinit var commandStudyRoomPort: StudentCommandStudyRoomPort

    @MockBean
    private lateinit var commandStudentPort: CommandStudentPort

    @MockBean
    private lateinit var commandUserPort: ManagerCommandUserPort

    private lateinit var removeStudentUseCase: RemoveStudentUseCase

    @BeforeEach
    fun setUp() {
        removeStudentUseCase = RemoveStudentUseCase(
            securityPort, queryUserPort, queryStudentPort, commandRemainStatusPort,
            commandStudyRoomPort, commandStudentPort, commandUserPort
        )
    }

    private val userStub by lazy {
        User(
            id = UUID.randomUUID(),
            schoolId = schoolId,
            accountId = "아이디",
            password = "비밀번호",
            email = "이메일",
            authority = Authority.MANAGER,
            createdAt = LocalDateTime.now(),
            deletedAt = null
        )
    }

    private val studentStub by lazy {
        Student(
            id = studentId,
            roomId = UUID.randomUUID(),
            roomNumber = "123",
            roomLocation = "A",
            schoolId = schoolId,
            grade = 2,
            classRoom = 3,
            number = 10,
            name = "이름",
            profileImageUrl = "https://~",
            sex = Sex.FEMALE
        )
    }

    private val seatStub by lazy {
        Seat(
            id = UUID.randomUUID(),
            studyRoomId = studyRoomId,
            typeId = UUID.randomUUID(),
            widthLocation = 1,
            heightLocation = 1,
            number = 1,
            status = SeatStatus.AVAILABLE
        )
    }

    private val managerId = UUID.randomUUID()
    private val studentId = UUID.randomUUID()
    private val schoolId = UUID.randomUUID()
    private val studyRoomId = UUID.randomUUID()

    @Test
    fun `학생 삭제 성공`() {
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
    fun `관리자 미존재`() {
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
    fun `학생 미존재`() {
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
    fun `학생 유저 미존재`() {
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
    fun `학교 불일치`() {
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
