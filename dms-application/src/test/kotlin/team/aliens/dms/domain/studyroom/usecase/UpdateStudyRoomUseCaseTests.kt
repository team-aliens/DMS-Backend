package team.aliens.dms.domain.studyroom.usecase

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.given
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.junit.jupiter.SpringExtension
import team.aliens.dms.domain.auth.model.Authority
import team.aliens.dms.domain.school.exception.SchoolMismatchException
import team.aliens.dms.domain.student.model.Sex
import team.aliens.dms.domain.studyroom.dto.CreateStudyRoomRequest
import team.aliens.dms.domain.studyroom.dto.UpdateStudyRoomRequest
import team.aliens.dms.domain.studyroom.exception.StudyRoomAlreadyExistsException
import team.aliens.dms.domain.studyroom.exception.StudyRoomNotFoundException
import team.aliens.dms.domain.studyroom.model.SeatStatus
import team.aliens.dms.domain.studyroom.model.StudyRoom
import team.aliens.dms.domain.studyroom.spi.CommandStudyRoomPort
import team.aliens.dms.domain.studyroom.spi.QueryStudyRoomPort
import team.aliens.dms.domain.studyroom.spi.StudyRoomQueryUserPort
import team.aliens.dms.domain.studyroom.spi.StudyRoomSecurityPort
import team.aliens.dms.domain.user.exception.UserNotFoundException
import team.aliens.dms.domain.user.model.User
import java.time.LocalDateTime
import java.util.UUID

@ExtendWith(SpringExtension::class)
class UpdateStudyRoomUseCaseTests {

    @MockBean
    private lateinit var queryStudyRoomPort: QueryStudyRoomPort

    @MockBean
    private lateinit var commandStudyRoomPort: CommandStudyRoomPort

    @MockBean
    private lateinit var securityPort: StudyRoomSecurityPort

    @MockBean
    private lateinit var queryUserPort: StudyRoomQueryUserPort

    private lateinit var updateStudyRoomUseCase: UpdateStudyRoomUseCase

    @BeforeEach
    fun setUp() {
        updateStudyRoomUseCase = UpdateStudyRoomUseCase(
            queryStudyRoomPort,
            commandStudyRoomPort,
            securityPort,
            queryUserPort
        )
    }

    private val studyRoomId = UUID.randomUUID()
    private val userId = UUID.randomUUID()
    private val schoolId = UUID.randomUUID()
    private val password = "test password"
    private val floor = 1
    private val studyRoomName = "test study room"

    private val userStub by lazy {
        User(
            id = userId,
            schoolId = schoolId,
            accountId = "test account id",
            password = password,
            email = "test email",
            authority = Authority.STUDENT,
            createdAt = LocalDateTime.now(),
            deletedAt = null
        )
    }

    private val studyRoomStub by lazy {
        StudyRoom(
            schoolId = userStub.schoolId,
            name = requestStub.name,
            floor = requestStub.floor,
            widthSize = requestStub.totalWidthSize,
            heightSize = requestStub.totalHeightSize,
            inUseHeadcount = 0,
            availableHeadcount = requestStub.seats.count { SeatStatus.AVAILABLE == SeatStatus.valueOf(it.status) },
            availableSex = Sex.valueOf(requestStub.availableSex),
            availableGrade = requestStub.availableGrade,
            eastDescription = requestStub.eastDescription,
            westDescription = requestStub.westDescription,
            southDescription = requestStub.southDescription,
            northDescription = requestStub.northDescription,
        )
    }

    private val requestStub by lazy {
        UpdateStudyRoomRequest(
            floor = 5,
            name = "new study room name",
            totalWidthSize = 10,
            totalHeightSize = 10,
            eastDescription = "eastDescription",
            westDescription = "westDescription",
            southDescription = "southDescription",
            northDescription = "northDescription",
            availableSex = "FEMALE",
            availableGrade = 2,
            seats = listOf(
                UpdateStudyRoomRequest.SeatRequest(
                    widthLocation = 1,
                    heightLocation = 2,
                    number = 1,
                    typeId = UUID.randomUUID(),
                    status = "AVAILABLE"
                )
            )
        )
    }

    @Test
    fun `자습실 수정 성공 - 층 & 이름이 동일하지 않음`() {
        // given
        given(securityPort.getCurrentUserId())
            .willReturn(userId)

        given(queryUserPort.queryUserById(userId))
            .willReturn(userStub)

        given(queryStudyRoomPort.queryStudyRoomById(studyRoomId))
            .willReturn(studyRoomStub)

        given(queryStudyRoomPort.existsStudyRoomByFloorAndNameAndSchoolId(floor, studyRoomName, schoolId))
            .willReturn(false)

        // when & then
        assertDoesNotThrow {
            updateStudyRoomUseCase.execute(studyRoomId, requestStub)
        }
    }

    @Test
    fun `자습실 수정 성공 - 층 & 이름이 동일`() {
        // given
        val requestStub = requestStub.copy(
            floor = floor,
            name = studyRoomName
        )

        given(securityPort.getCurrentUserId())
            .willReturn(userId)

        given(queryUserPort.queryUserById(userId))
            .willReturn(userStub)

        given(queryStudyRoomPort.queryStudyRoomById(studyRoomId))
            .willReturn(studyRoomStub)

        // when & then
        assertDoesNotThrow {
            updateStudyRoomUseCase.execute(studyRoomId, requestStub)
        }
    }

    @Test
    fun `유저를 찾을 수 없음`() {
        // given
        given(securityPort.getCurrentUserId())
            .willReturn(userId)

        given(queryUserPort.queryUserById(userId))
            .willReturn(null)

        // when & then
        assertThrows<UserNotFoundException> {
            updateStudyRoomUseCase.execute(studyRoomId, requestStub)
        }
    }

    @Test
    fun `자습실을 찾을 수 없음`() {
        // given
        given(securityPort.getCurrentUserId())
            .willReturn(userId)

        given(queryUserPort.queryUserById(userId))
            .willReturn(userStub)

        given(queryStudyRoomPort.queryStudyRoomById(studyRoomId))
            .willReturn(null)

        // when & then
        assertThrows<StudyRoomNotFoundException> {
            updateStudyRoomUseCase.execute(studyRoomId, requestStub)
        }
    }

    @Test
    fun `학교가 일치하지 않음`() {
        // given
        given(securityPort.getCurrentUserId())
            .willReturn(userId)

        given(queryUserPort.queryUserById(userId))
            .willReturn(userStub)

        given(queryStudyRoomPort.queryStudyRoomById(studyRoomId))
            .willReturn(
                studyRoomStub.copy(schoolId = UUID.randomUUID())
            )

        // when & then
        assertThrows<SchoolMismatchException> {
            updateStudyRoomUseCase.execute(studyRoomId, requestStub)
        }
    }

    @Test
    fun `같은 층, 이름의 자습실이 이미 존재함`() {
        // given
        given(securityPort.getCurrentUserId())
            .willReturn(userId)

        given(queryUserPort.queryUserById(userId))
            .willReturn(userStub)

        given(queryStudyRoomPort.queryStudyRoomById(studyRoomId))
            .willReturn(
                studyRoomStub.copy(
                    floor = floor,
                    name = studyRoomName
                )
            )

        given(queryStudyRoomPort.existsStudyRoomByFloorAndNameAndSchoolId(requestStub.floor, requestStub.name, schoolId))
            .willReturn(true)

        // when & then
        assertThrows<StudyRoomAlreadyExistsException> {
            updateStudyRoomUseCase.execute(studyRoomId, requestStub)
        }
    }
}