package team.aliens.dms.domain.studyroom.usecase

import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import java.util.UUID
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import team.aliens.dms.domain.studyroom.dto.CreateStudyRoomRequest
import team.aliens.dms.domain.studyroom.exception.StudyRoomAlreadyExistsException
import team.aliens.dms.domain.studyroom.model.SeatStatus
import team.aliens.dms.domain.studyroom.model.StudyRoom
import team.aliens.dms.domain.studyroom.spi.CommandStudyRoomPort
import team.aliens.dms.domain.studyroom.spi.QueryStudyRoomPort
import team.aliens.dms.domain.studyroom.spi.StudyRoomQueryUserPort
import team.aliens.dms.domain.studyroom.spi.StudyRoomSecurityPort
import team.aliens.dms.domain.studyroom.stub.createStudyRoomStub
import team.aliens.dms.domain.studyroom.stub.createTimeSlotStub
import team.aliens.dms.domain.user.exception.UserNotFoundException
import team.aliens.dms.domain.user.stub.createUserStub

class CreateStudyRoomUseCaseTests {

    private val queryStudyRoomPort: QueryStudyRoomPort = mockk(relaxed = true)
    private val commandStudyRoomPort: CommandStudyRoomPort = mockk(relaxed = true)
    private val securityPort: StudyRoomSecurityPort = mockk(relaxed = true)
    private val queryUserPort: StudyRoomQueryUserPort = mockk(relaxed = true)

    private val createStudyRoomUseCase = CreateStudyRoomUseCase(
        queryStudyRoomPort, commandStudyRoomPort, securityPort, queryUserPort
    )

    private val managerId = UUID.randomUUID()
    private val schoolId = UUID.randomUUID()

    private val userStub by lazy {
        createUserStub(
            id = managerId,
            schoolId = schoolId
        )
    }

    private val studyRoomStub by lazy {
        createStudyRoomStub(
            availableHeadcount = requestStub.seats.count {
                SeatStatus.AVAILABLE == SeatStatus.valueOf(it.status)
            }
        )
    }

    private val requestStub by lazy {
        CreateStudyRoomRequest(
            floor = 1,
            name = "studyRoomName",
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
                CreateStudyRoomRequest.SeatRequest(
                    widthLocation = 1,
                    heightLocation = 2,
                    number = 1,
                    typeId = UUID.randomUUID(),
                    status = "AVAILABLE"
                )
            )
        )
    }

    private val timeSlotStub by lazy {
        createTimeSlotStub(
            schoolId = schoolId
        )
    }

    @Test
    fun `자습실 생성 성공`() {
        // given
        every { securityPort.getCurrentUserId() } returns managerId
        every { queryUserPort.queryUserById(managerId) } returns userStub
        every {
            queryStudyRoomPort.existsStudyRoomByFloorAndNameAndSchoolId(
                requestStub.floor,
                requestStub.name,
                schoolId
            )
        } returns false

        val studyRoomSlot = slot<StudyRoom>()
        every { commandStudyRoomPort.saveStudyRoom(capture(studyRoomSlot)) } returns studyRoomStub
        every { queryStudyRoomPort.queryTimeSlotsBySchoolId(schoolId) } returns listOf(timeSlotStub)

        // when & then
        assertAll(
            {
                assertDoesNotThrow {
                    createStudyRoomUseCase.execute(requestStub)
                }
            },
            {
                assertEquals(
                    studyRoomSlot.captured.availableHeadcount,
                    requestStub.seats.count { it.status == SeatStatus.AVAILABLE.name }
                )
            }
        )
    }

    @Test
    fun `유저가 존재하지 않음`() {
        // given
        every { securityPort.getCurrentUserId() } returns managerId
        every { queryUserPort.queryUserById(managerId) } returns null

        // when & then
        assertThrows<UserNotFoundException> {
            createStudyRoomUseCase.execute(requestStub)
        }
    }

    @Test
    fun `같은 이름, 층의 자습실이 이미 존재함`() {
        // given
        every { securityPort.getCurrentUserId() } returns managerId
        every { queryUserPort.queryUserById(managerId) } returns userStub
        every {
            queryStudyRoomPort.existsStudyRoomByFloorAndNameAndSchoolId(requestStub.floor, requestStub.name, schoolId)
        } returns true

        // when & then
        assertThrows<StudyRoomAlreadyExistsException> {
            createStudyRoomUseCase.execute(requestStub)
        }
    }
}
