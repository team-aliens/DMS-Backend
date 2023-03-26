package team.aliens.dms.domain.studyroom.usecase

import io.mockk.every
import io.mockk.mockk
import java.util.UUID
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import team.aliens.dms.domain.studyroom.dto.UpdateStudyRoomRequest
import team.aliens.dms.domain.studyroom.exception.StudyRoomAlreadyExistsException
import team.aliens.dms.domain.studyroom.exception.StudyRoomNotFoundException
import team.aliens.dms.domain.studyroom.spi.CommandStudyRoomPort
import team.aliens.dms.domain.studyroom.spi.QueryStudyRoomPort
import team.aliens.dms.domain.studyroom.spi.StudyRoomQueryUserPort
import team.aliens.dms.domain.studyroom.spi.StudyRoomSecurityPort
import team.aliens.dms.domain.studyroom.stub.createStudyRoomStub
import team.aliens.dms.domain.user.exception.UserNotFoundException
import team.aliens.dms.domain.user.stub.createUserStub

class UpdateStudyRoomUseCaseTests {

    private val queryStudyRoomPort: QueryStudyRoomPort = mockk(relaxed = true)
    private val commandStudyRoomPort: CommandStudyRoomPort = mockk(relaxed = true)
    private val securityPort: StudyRoomSecurityPort = mockk(relaxed = true)
    private val queryUserPort: StudyRoomQueryUserPort = mockk(relaxed = true)

    private val updateStudyRoomUseCase = UpdateStudyRoomUseCase(
        queryStudyRoomPort, commandStudyRoomPort, securityPort, queryUserPort
    )

    private val userId = UUID.randomUUID()
    private val schoolId = UUID.randomUUID()
    private val studyRoomId = UUID.randomUUID()

    private val userStub by lazy {
        createUserStub(
            id = userId,
            schoolId = schoolId
        )
    }

    private val studyRoomStub by lazy {
        createStudyRoomStub(
            schoolId = schoolId,
            floor = requestStub.floor,
            name = requestStub.name,
        )
    }

    private val requestStub by lazy {
        UpdateStudyRoomRequest(
            floor = 1,
            name = "",
            totalWidthSize = 10,
            totalHeightSize = 10,
            eastDescription = "eastDescription",
            westDescription = "westDescription",
            southDescription = "southDescription",
            northDescription = "northDescription",
            availableSex = "FEMALE",
            availableGrade = 2,
            timeSlotIds = listOf(UUID.randomUUID()),
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
        val floor = 9
        val name = "qweqwe"

        every { securityPort.getCurrentUserId() } returns userId
        every { queryUserPort.queryUserById(userId) } returns userStub
        every {
            queryStudyRoomPort.queryStudyRoomById(studyRoomId)
        } returns studyRoomStub.copy(
            floor = floor,
            name = name
        )
        every { queryStudyRoomPort.existsStudyRoomByFloorAndNameAndSchoolId(floor, name, schoolId) } returns false

        // when & then
        assertDoesNotThrow {
            updateStudyRoomUseCase.execute(studyRoomId, requestStub)
        }
    }

    @Test
    fun `자습실 수정 성공 - 층 & 이름이 동일`() {
        // given
        every { securityPort.getCurrentUserId() } returns userId
        every { queryUserPort.queryUserById(userId) } returns userStub
        every { queryStudyRoomPort.queryStudyRoomById(studyRoomId) } returns studyRoomStub

        // when & then
        assertDoesNotThrow {
            updateStudyRoomUseCase.execute(studyRoomId, requestStub)
        }
    }

    @Test
    fun `유저를 찾을 수 없음`() {
        // given
        every { securityPort.getCurrentUserId() } returns userId
        every { queryUserPort.queryUserById(userId) } returns null

        // when & then
        assertThrows<UserNotFoundException> {
            updateStudyRoomUseCase.execute(studyRoomId, requestStub)
        }
    }

    @Test
    fun `자습실 정보를 찾을 수 없음`() {
        // given
        every { securityPort.getCurrentUserId() } returns userId
        every { queryUserPort.queryUserById(userId) } returns userStub
        every { queryStudyRoomPort.queryStudyRoomById(studyRoomId) } returns null

        // when & then
        assertThrows<StudyRoomNotFoundException> {
            updateStudyRoomUseCase.execute(studyRoomId, requestStub)
        }
    }

    @Test
    fun `같은 층, 이름의 자습실이 이미 존재함`() {
        // given
        val floor = 9
        val name = "qweqwe"

        every { securityPort.getCurrentUserId() } returns userId
        every { queryUserPort.queryUserById(userId) } returns userStub
        every {
            queryStudyRoomPort.queryStudyRoomById(studyRoomId)
        } returns studyRoomStub.copy(
            floor = floor,
            name = name
        )
        every { queryStudyRoomPort.existsStudyRoomByFloorAndNameAndSchoolId(requestStub.floor, requestStub.name, schoolId) } returns true

        // when & then
        assertThrows<StudyRoomAlreadyExistsException> {
            updateStudyRoomUseCase.execute(studyRoomId, requestStub)
        }
    }
}
